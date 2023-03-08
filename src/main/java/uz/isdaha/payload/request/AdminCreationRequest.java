package uz.isdaha.payload.request;

import lombok.Data;
import uz.isdaha.constan.RestApiConstants;
import uz.isdaha.enums.RoleEnum;
import uz.isdaha.payload.Creatable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AdminCreationRequest implements Creatable {

    @Pattern(regexp = RestApiConstants.PHONE_NUMBER_REGEX,
        message = "Telefon raqam formati xato. Telefon raqam 998 bilan boshlanib, 9 tadan 15 tagacha sonlarda iborat bo'lishi kerak")
    @Size(min = 12, max = 12 , message = "99899****** format must be 12 character")
    private String phoneNumber;

    @Size(min = 6 , message = "password must be more 6 character")
    private String password;

    private String firstName;

    private RoleEnum roleEnum;
}
