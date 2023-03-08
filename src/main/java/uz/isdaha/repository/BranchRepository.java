package uz.isdaha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.isdaha.entity.Branch;

import java.util.Optional;

@Repository

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByIdAndDeletedIsFalse(Long id);
}
