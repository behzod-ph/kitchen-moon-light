package uz.isdaha.service.address;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.isdaha.entity.Address;
import uz.isdaha.entity.User;
import uz.isdaha.exception.AddressNotFoundException;
import uz.isdaha.payload.request.AddressRequest;
import uz.isdaha.payload.response.AddressResponse;
import uz.isdaha.repository.AddressRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImp implements AddressService {

    private final AddressRepository repository;

    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<?> create(AddressRequest addressRequest) {
        Address address = modelMapper.map(addressRequest, Address.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        address.setUser(user);
        return ResponseEntity.ok(AddressResponse.toDto(repository.save(address)));
    }


    @Override
    public ResponseEntity<?> update(Long id, AddressRequest addressRequest) {
        get(id);
        Address address = modelMapper.map(addressRequest, Address.class);
        return ResponseEntity.ok(AddressResponse.toDto(repository.save(address)));
    }

    public ResponseEntity<?> getAllAddressPages(Pageable pageable) {
        List<AddressResponse> content = repository.findAll(pageable).getContent().stream().map(AddressResponse::toDto
        ).collect(Collectors.toList());
        return ResponseEntity.ok(
            content
        );
    }

    @Override
    public ResponseEntity<?> get(Long id) {
        Address address = repository.findById(id).orElseThrow(AddressNotFoundException::new);
        return ResponseEntity.ok(AddressResponse.toDto(address));
    }


    @Override
    public ResponseEntity<?> delete(Long id) {
        get(id);
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page> pageOf(Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<List<AddressResponse>> getByUserAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(repository.findByUser(user).stream().map(AddressResponse::toDto).collect(Collectors.toList()));
    }
}
