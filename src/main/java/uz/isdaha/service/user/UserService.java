package uz.isdaha.service.user;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uz.isdaha.constan.MessageConstants;
import uz.isdaha.constan.RestApiConstants;
import uz.isdaha.entity.*;
import uz.isdaha.enums.DeviceType;
import uz.isdaha.enums.ErrorCode;
import uz.isdaha.enums.RoleEnum;
import uz.isdaha.exception.RestException;
import uz.isdaha.exception.RoleNotFoundException;
import uz.isdaha.exception.UserNotFoundException;
import uz.isdaha.payload.request.AdminCreationRequest;
import uz.isdaha.payload.request.UpdateUserRequest;
import uz.isdaha.payload.request.UserRegistrationRequest;
import uz.isdaha.payload.request.UserLoginDto;
import uz.isdaha.payload.response.UserLoginResponse;
import uz.isdaha.payload.response.UserResponse;
import uz.isdaha.repository.*;
import uz.isdaha.payload.response.Response;
import uz.isdaha.security.JwtProvider;
import uz.isdaha.integration.sms.SmsFeign;

import java.util.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static uz.isdaha.constan.RestApiConstants.TEMPLATE_SEND_ACTIVATION;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final SmsFeign smsClient;

    private final SmsCodeRepository smsCodeRepository;

    public ResponseEntity<?> loginAdmin(UserLoginDto loginDto) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            loginDto.getPhonenumber(),
            loginDto.getPassword());
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authentication);
        } catch (Exception e) {
            throw new RestException(ErrorCode.PHONE_NUMBER_OR_PASSWORD_INCORRECT);
        }
        User user = (User) authenticate.getPrincipal();
        return ResponseEntity.ok().body(new UserLoginResponse(user.getId(), user.getRole().stream().map(Role::getRole).collect(Collectors.toList()), "Bearer " + jwtProvider.createToken(user)));
    }

    public ResponseEntity<?> addAdmin(AdminCreationRequest adminCreationRequest) {
        phoneNumberIfExistThrow(adminCreationRequest.getPhoneNumber());
        User newUser = modelMapper.map(adminCreationRequest, User.class);

        Role role = roleRepository.findByRole(adminCreationRequest.getRoleEnum())
            .orElseThrow(RoleNotFoundException::new);
        newUser.setRole(Collections.singleton(role));
        newUser.setActive(true);
        newUser.setDevice(DeviceType.WEB);
        newUser.setPassword(passwordEncoder.encode(adminCreationRequest.getPassword()));
        userRepository.save(newUser);
        return ResponseEntity.ok("Success admin ");
    }

    public Response banAdmin(long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setActive(false);
        userRepository.save(user);
        return new Response("Admin ban success", 10);
    }

    public ResponseEntity<?> registerUsers(UserRegistrationRequest request) {
        phoneNumberIfExistThrow(request.getPhoneNumber());
        Role role = roleRepository.findByRole(RoleEnum.USER)
            .orElseThrow(RoleNotFoundException::new);

        User user = User.builder()
            .firstName(request.getFirstName())
            .deviceToken(request.getDeviceToken())
            .device(request.getDevice())
            .role(Collections.singleton(role))
            .phoneNumber(request.getPhoneNumber())
            .password(passwordEncoder.encode("root"))
            .build();

        User save = userRepository.save(user);
        sendSmsCode(save.getPhoneNumber());
        return ResponseEntity.ok(save.getPhoneNumber());
    }

    public ResponseEntity<?> loginUser(String phoneNumber) {
        boolean exists = userRepository.existsByPhoneNumber(phoneNumber);
        if (!exists) {
            throw new RestException(ErrorCode.USER_NOT_FOUND_OR_DISABLED);
        }
        sendSmsCode(phoneNumber);
        return ResponseEntity.ok(phoneNumber);
    }

    public ResponseEntity<?> verifyCode(String phoneNumber, String code) {
        SmsCode smsCode = smsCodeRepository.findFirstByPhoneNumberOrderByCreatedAtDesc(phoneNumber).orElse(null);

        if (smsCode != null && code.equals(smsCode.getCode()) && !smsCode.getChecked() &&
            (System.currentTimeMillis() - smsCode.getCreatedAt().getTime() < TimeUnit.MINUTES.toMillis(6))) {
            User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RestException(ErrorCode.USER_NOT_FOUND_OR_DISABLED));
            smsCode.setChecked(true);
            user.setActive(true);
            userRepository.save(user);
            smsCodeRepository.save(smsCode);
            return ResponseEntity.ok(new UserLoginResponse(user.getId(), user.getRole().stream().map(Role::getRole).collect(Collectors.toList()), "Bearer " + jwtProvider.createToken(user)));
        }
        return ResponseEntity.ok(new Response(MessageConstants.SMS_CODE_INCORRECT, RestApiConstants.INVALID));
    }

    public void phoneNumberIfExistThrow(String phoneNumber) {

        if (userRepository.existsByPhoneNumber(phoneNumber))
            throw new RestException(ErrorCode.USER_ALREADY_REGISTERED);
    }

    public void sendSmsCode(String phoneNumber) {

        if ((smsCodeRepository.overLimitSmsCodeByPhoneNumberAndDuration(5, phoneNumber, 1))) {
            throw new RestException(ErrorCode.MANY_ATTEMPTS);
        }

        SmsCode oldSmsCode = smsCodeRepository.findFirstByPhoneNumberOrderByCreatedAtDesc(phoneNumber).orElse(null);
        if (oldSmsCode != null) {
            oldSmsCode.setChecked(false);
            oldSmsCode.setIgnored(true);
            smsCodeRepository.save(oldSmsCode);
        }
        SmsCode smsCode = SmsCode.builder()
            .code(generateCode())
            .phoneNumber(phoneNumber).
            build();

        smsCode = smsCodeRepository.save(smsCode);
        smsClient.sendActivationCode(getMapSms(smsCode.getPhoneNumber(), TEMPLATE_SEND_ACTIVATION + smsCode.getCode()));
    }

    public UserResponse updateUser(Long userId, String name) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setFirstName(name);
        User save = userRepository.save(user);
        return UserResponse.toDto(save);
    }


    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setFirstName(request.getName());
        Set<Role> role1 = new HashSet<>();
        request.getRoles().forEach(i -> {
            Role role = roleRepository.findByRole(i).orElseThrow(RoleNotFoundException::new);
            role1.add(role);
        });
        user.setRole(role1);
        User save = userRepository.save(user);
        return UserResponse.toDto(save);
    }

    private String generateCode() {
        Random random = new Random();
        return String.format("%05d", random.nextInt(100000));
    }

    public ResponseEntity<Page<UserResponse>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAll(pageable).map(UserResponse::toDto));
    }

    public ResponseEntity<UserResponse> getById(long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(UserResponse.toDto(user));
    }

    private Map<String, String> getMapSms(String phone, String textMessage) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile_phone", phone);
        map.put("message", textMessage);
        map.put("from", "4546");
        return map;
    }
}
