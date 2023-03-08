package uz.isdaha.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.isdaha.entity.Category;
import uz.isdaha.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByCategoryAndAvailableTrue(Category category);

    Page<Product> findAllByAvailable(Boolean available, Pageable pageable);

    @Query("select imageUrl from Product where id = ?1")
    String findImageUrlById(Long id);

    Optional<Product> findByIdAndAvailableTrue(Long id);

}

