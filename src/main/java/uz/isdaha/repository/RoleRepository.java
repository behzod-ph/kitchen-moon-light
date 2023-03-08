package uz.isdaha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.isdaha.entity.Role;
import uz.isdaha.enums.RoleEnum;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role ,Long> {

    Optional<Role> findByRole(RoleEnum name);



}
