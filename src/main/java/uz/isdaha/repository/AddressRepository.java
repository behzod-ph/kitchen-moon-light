package uz.isdaha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isdaha.entity.Address;
import uz.isdaha.entity.User;

import java.util.List;

@Repository

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
