package uz.isdaha.payload.request;

import lombok.Data;
import uz.isdaha.enums.RoleEnum;

import java.util.List;

@Data
public class UpdateUserRequest {
    private String name;
    private List<RoleEnum> roles;
}
