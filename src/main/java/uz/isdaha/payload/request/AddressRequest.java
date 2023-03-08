package uz.isdaha.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isdaha.payload.Creatable;
import uz.isdaha.payload.Modifiable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest implements Creatable, Modifiable {

    private String district;

    private String street;

    private String home;

    private String entrance;

    private String homeNumber;

    private double latitude;

    private double longitude;

}
