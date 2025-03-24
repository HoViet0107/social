package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.social.model.Roles;
import personal.social.model.User;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    List<Roles> findByUser(User user);
}
