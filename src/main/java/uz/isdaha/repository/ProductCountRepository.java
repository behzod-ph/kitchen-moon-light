package uz.isdaha.repository;

import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.isdaha.entity.ProductCountOrder;

public interface ProductCountRepository extends JpaRepository<ProductCountOrder, Integer> {
}
