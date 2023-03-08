package uz.isdaha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.isdaha.entity.FeeDistance;

import java.util.List;

public interface FeeDistanceRepository extends JpaRepository<FeeDistance, Long> {
    List<FeeDistance> findByOrderByCreatedAtDesc();
}
