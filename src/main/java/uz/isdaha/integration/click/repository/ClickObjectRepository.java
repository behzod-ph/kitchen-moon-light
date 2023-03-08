package uz.isdaha.integration.click.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.isdaha.integration.click.entity.ClickObject;

public interface ClickObjectRepository extends JpaRepository<ClickObject, Long> {
}
