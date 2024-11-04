package pl.mateusz.example.friendoo.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@SuppressWarnings({"checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocType"})
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByEmail(String email);
  @Query(value = "SELECT f.* FROM users u JOIN user_friends uf ON u.id = uf.user_id "
      + "JOIN users f ON uf.friend_id = f.id WHERE u.email = :email", nativeQuery = true)
  List<User> findFriendsByEmail(@Param("email") String email);


}
