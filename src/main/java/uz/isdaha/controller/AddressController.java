package uz.isdaha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.isdaha.payload.request.AddressRequest;
import uz.isdaha.service.address.AddressService;

@RestController
@RequestMapping("api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Validated AddressRequest addressDto) {
        return addressService.create(addressDto);
    }

    @GetMapping("all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllAddressByUser() {
        return ResponseEntity.ok(addressService.getByUserAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return addressService.get(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAll(Pageable pageable) {
        return addressService.pageOf(pageable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Validated AddressRequest addressDto) {
        return addressService.update(id, addressDto);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        return addressService.delete(id);
    }
}
