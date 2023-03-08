package uz.isdaha.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.isdaha.payload.request.AdminCreationRequest;
import uz.isdaha.payload.request.UserRegistrationRequest;
import uz.isdaha.payload.request.UserLoginDto;
import uz.isdaha.service.user.UserService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final UserService userservice;

    public AuthController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping("register_user")
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserRegistrationRequest userReq) {
        return ResponseEntity.ok(userservice.registerUsers(userReq));
    }


    @PostMapping("add_admin")
    public ResponseEntity<?> registerAdmin(@RequestBody @Validated AdminCreationRequest adminCreationRequest) {
        return ResponseEntity.ok(userservice.addAdmin(adminCreationRequest));
    }


    @PostMapping("login_admin")
    public ResponseEntity<?> loginAdmin(@RequestBody @Validated UserLoginDto loginDto) {

        return ResponseEntity.ok(
            userservice.loginAdmin(loginDto)
        );
    }


    @PostMapping("login_user")
    public ResponseEntity<?> loginUser(@RequestParam("phonenumber") String phoneNumber) {
        return ResponseEntity.ok(
            userservice.loginUser(phoneNumber)
        );
    }


    @GetMapping("verify_code")
    public ResponseEntity<?> verifyUser(@RequestParam("phone_number") String phoneNumber,
                                        @RequestParam("code") String code) {
        return ResponseEntity.ok(userservice.verifyCode(phoneNumber, code));
    }

    @GetMapping("send_code")
    public ResponseEntity<?> sendActivationCode(@RequestParam("phone_number") String phoneNumber) {
        userservice.sendSmsCode(phoneNumber);
        return ResponseEntity.ok().build();
    }

}
