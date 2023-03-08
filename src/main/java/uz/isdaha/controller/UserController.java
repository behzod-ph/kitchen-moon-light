package uz.isdaha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.isdaha.payload.request.UpdateUserRequest;
import uz.isdaha.service.user.UserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("ban_admin")
    public ResponseEntity<?> banAdmin(@RequestParam("id") long id) {
        return ResponseEntity.ok(userService.banAdmin(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }


    @PreAuthorize("hasRole('ROLE_MANAGER') or  hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or  hasRole('ROLE_ADMIN')")
    @PutMapping("admin/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("admin-update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestParam("name") String name) {
        return ResponseEntity.ok(userService.updateUser(id, name));
    }
}
