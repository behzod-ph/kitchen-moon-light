package uz.isdaha.service.fee;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.isdaha.entity.FeeDistance;
import uz.isdaha.payload.request.FeeDistanceRequest;
import uz.isdaha.payload.response.FeeDistanceResponse;
import uz.isdaha.repository.FeeDistanceRepository;

@RequiredArgsConstructor
@Service
public class FeeDistanceServiceImpl implements FeeDistanceService {

    private final FeeDistanceRepository repository;

    @Override
    public ResponseEntity<?> add(FeeDistanceRequest request) {
        FeeDistance feeDistance = new FeeDistance(request.getPrice(), request.getDeliveryPayment());
        return ResponseEntity.ok(FeeDistanceResponse.toDto(repository.save(feeDistance)));
    }

    @Override
    public ResponseEntity<?> get(Long id) {
        FeeDistance feeDistance = repository.findById(id).orElseThrow(() -> new RuntimeException("this id not found fee"));
        return ResponseEntity.ok(FeeDistanceResponse.toDto(repository.save(feeDistance)));
    }

    @Override
    public ResponseEntity<?> getLast() {
        FeeDistance feeDistance = repository.findByOrderByCreatedAtDesc().stream().findFirst().orElseThrow((() -> new RuntimeException("this id not found fee")));
        return ResponseEntity.ok(FeeDistanceResponse.toDto(feeDistance));
    }

    @Override
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(repository.findAll().stream().map(FeeDistanceResponse::toDto));
    }

    @Override
    public ResponseEntity<?> update(long id, FeeDistanceRequest request) {
        return ResponseEntity.ok(FeeDistanceResponse.toDto(repository.save(new FeeDistance(id, request.getPrice(), request.getDeliveryPayment()))));
    }
}
