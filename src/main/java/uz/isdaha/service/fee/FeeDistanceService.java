package uz.isdaha.service.fee;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import uz.isdaha.payload.request.FeeDistanceRequest;


public interface FeeDistanceService {
    ResponseEntity<?> add(FeeDistanceRequest request);

    ResponseEntity<?> get(Long id);

    ResponseEntity<?> getLast();

    ResponseEntity<?> all();

    ResponseEntity<?> update(long id, FeeDistanceRequest request);
}
