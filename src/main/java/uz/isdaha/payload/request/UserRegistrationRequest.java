package uz.isdaha.payload.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.NotBlank;
import uz.isdaha.constan.RestApiConstants;
import uz.isdaha.enums.DeviceType;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegistrationRequest {

    @Column(nullable = false)
    String firstName;

    @NotNull
    String deviceToken;

    DeviceType device;

    @Size(min = 12, max = 12, message = "99899****** format must be 12 character")
    @Pattern(regexp = RestApiConstants.PHONE_NUMBER_REGEX,
        message = "Telefon raqam formati xato. Telefon raqam 998 bilan boshlanib, 9 tadan 15 tagacha sonlarda iborat bo'lishi kerak")
    String phoneNumber;
}
