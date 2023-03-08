package uz.isdaha.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isdaha.entity.Role;
import uz.isdaha.enums.RoleEnum;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    private long id;
    private List<RoleEnum> roles;
    private String token;
}
