package uz.isdaha.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isdaha.constan.RestApiConstants;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginDto {
    @Pattern(regexp = RestApiConstants.PHONE_NUMBER_REGEX,
        message = "Telefon raqam formati xato. Telefon raqam 998 bilan boshlanib, 9 tadan 15 tagacha sonlarda iborat bo'lishi kerak")
    @Size(min = 12, max = 12, message = "99899****** format must be 12 character")
    private String phonenumber;
    private String password;
}
