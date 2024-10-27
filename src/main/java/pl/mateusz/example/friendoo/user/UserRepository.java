package pl.mateusz.example.friendoo.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings({"checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocType"})
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByEmail(String email);
}
