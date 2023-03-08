package uz.isdaha.service.address;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uz.isdaha.payload.request.AddressRequest;
import uz.isdaha.payload.response.AddressResponse;
import uz.isdaha.service.CRUDService;
import uz.isdaha.service.PageProducer;

import java.util.List;

@Component
public interface AddressService extends CRUDService<AddressRequest, AddressRequest, Long>, PageProducer {
    ResponseEntity<List<AddressResponse>> getByUserAll();
}
