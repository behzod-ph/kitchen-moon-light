package uz.isdaha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.isdaha.payload.request.FeeDistanceRequest;
import uz.isdaha.service.fee.FeeDistanceService;

@RestController
@RequestMapping("api/v1/fee")
@PreAuthorize(("hasRole('ROLE_ADMIN')"))
@RequiredArgsConstructor
public class FeeDistanceController {
    private final FeeDistanceService service;


    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Validated FeeDistanceRequest request) {
        return service.add(request);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return service.all();
    }

    @GetMapping("last")
    public ResponseEntity<?> getLast() {
        return service.getLast();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Validated FeeDistanceRequest request) {
        return service.update(id, request);
    }


}
