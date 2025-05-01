package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.social.enums.RoleEnum;
import personal.social.model.Roles;
import personal.social.model.Users;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    List<Roles> findByUser(Users user);

    @Query(value = "SELECT r FROM Roles r WHERE r.role =:roleEnum")
    Roles findByRole(RoleEnum roleEnum);
}
