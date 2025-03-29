package pl.mateusz.example.friendoo.user.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository for user roles.
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  Optional<UserRole> getUserRoleByRole(Role role);
}
