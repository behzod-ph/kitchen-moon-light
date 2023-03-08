package uz.isdaha.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isdaha.entity.Role;
import uz.isdaha.entity.User;
import uz.isdaha.enums.RoleEnum;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private long id;
    private String phoneNumber;
    private String firstName;
    private List<RoleEnum> roles;

    static public UserResponse toDto(User user) {
        return new UserResponse(user.getId(), user.getPhoneNumber(), user.getFirstName(),
            user.getRole().stream().map(Role::getRole).collect(Collectors.toList()));
    }
}
