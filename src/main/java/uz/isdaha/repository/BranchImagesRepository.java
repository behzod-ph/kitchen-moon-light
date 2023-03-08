package uz.isdaha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isdaha.entity.Branch;
import uz.isdaha.entity.BranchesImages;

import java.util.Optional;

@Repository

public interface BranchImagesRepository extends JpaRepository<BranchesImages, Integer> {
    BranchesImages getByImage(String image);


    boolean existsByImage(String image);
}
