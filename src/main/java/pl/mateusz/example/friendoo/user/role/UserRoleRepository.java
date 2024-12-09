package pl.mateusz.example.friendoo.user.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


@SuppressWarnings("checkstyle:MissingJavadocType")
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  Optional<UserRole> getUserRoleByRole(Role role);
}
