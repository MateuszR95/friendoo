package pl.mateusz.example.friendoo.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({"checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocType"})
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByEmail(String email);

  boolean existsUserByEmail(String email);

  @Query(value = "SELECT f.* FROM users u JOIN user_friends uf ON u.id = uf.user_id "
      + "JOIN users f ON uf.friend_id = f.id WHERE u.email = :email", nativeQuery = true)
  List<User> findFriendsByEmail(@Param("email") String email);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM users WHERE is_active_account = false AND id IN ("
      + "SELECT u.id FROM users u LEFT JOIN user_activation_token uat ON u.id = uat.user_id "
      + "WHERE uat.expire_date < CURRENT_TIMESTAMP OR uat.id IS NULL)", nativeQuery = true)
  void deleteInactiveUsersWithoutValidActivationToken();

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM users_roles WHERE user_id IN ("
      + "SELECT u.id FROM users u LEFT JOIN user_activation_token uat ON u.id = uat.user_id "
      + "WHERE u.is_active_account = false AND (uat.expire_date < CURRENT_TIMESTAMP OR uat.id"
      + " IS NULL))", nativeQuery = true)
  void deleteRolesForInactiveUsersWithoutValidActivationToken();


}
