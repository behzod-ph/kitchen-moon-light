package uz.isdaha.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.isdaha.entity.Address;

@Data
@AllArgsConstructor
public class AddressResponse {

    private Long id;
    private String district;
    private String street;
    private String home;
    private String entrance;
    private String homeNumber;
    private double latitude;
    private double longitude;
    private double userId;

    public static AddressResponse toDto(Address address) {
        return new AddressResponse(
            address.getId(),
            address.getDistrict(),
            address.getStreet(),
            address.getHome(),
            address.getEntrance(),
            address.getHomeNumber(),
            address.getLatitude(),
            address.getLongitude(),
            address.getUser().getId()
        );
    }
}
