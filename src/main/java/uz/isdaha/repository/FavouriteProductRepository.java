package uz.isdaha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isdaha.entity.Address;
import uz.isdaha.entity.FavoriteProduct;
import uz.isdaha.entity.Product;
import uz.isdaha.entity.User;

import java.util.List;
import java.util.Optional;

@Repository

public interface FavouriteProductRepository extends JpaRepository<FavoriteProduct, Integer> {
    Optional<FavoriteProduct> findByProduct(Product product);

    Optional<FavoriteProduct> findByUserAndProduct(User user, Product product);


    List<FavoriteProduct> findByUserAndFavouriteTrue(User user);


}
