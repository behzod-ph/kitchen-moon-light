package uz.isdaha.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import uz.isdaha.entity.Role;
import uz.isdaha.entity.User;
import uz.isdaha.enums.DeviceType;
import uz.isdaha.enums.RoleEnum;
import uz.isdaha.repository.RoleRepository;
import uz.isdaha.repository.UserRepository;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository repository;
    private final UserRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {

        for (RoleEnum value : RoleEnum.values()) {
            if (repository.findByRole(value).isEmpty()) {
                repository.save(new Role(value));
            }
        }


        if (!clientRepository.existsByPhoneNumber("Paycom")) {
            clientRepository.save(User.builder()
                .password(passwordEncoder.encode("wcJPs7UvFUavmvERUvfNp9eo0QZJ7cbgdBCH"))
                .firstName("Paycom")
                .phoneNumber("Paycom")
                .device(DeviceType.WEB)
                .isActive(true)
                .build());
        }

        if (!clientRepository.existsByPhoneNumber("998976331809")) {
            User user = User.builder()
                .password(passwordEncoder.encode("root123"))
                .firstName("Developer")
                .phoneNumber("998976331809")
                .device(DeviceType.WEB)
                .isActive(true)
                .build();
            HashSet<Role> roles = new HashSet<>();
            roles.add(repository.findByRole(RoleEnum.USER).orElse(null));
            roles.add(repository.findByRole(RoleEnum.ADMIN).orElse(null));

            user.setRole(roles);
            clientRepository.save(user);
        }
    }
}
