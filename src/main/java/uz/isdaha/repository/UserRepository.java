package uz.isdaha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isdaha.entity.Role;
import uz.isdaha.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIsActiveTrue(String phoneNumber);

    Optional<User> findByPhoneNumberAndIsActiveTrue(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByRoleAndDeletedFalse(Role role);
}
