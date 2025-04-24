package pl.mateusz.example.friendoo.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for users.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
  Optional<User> findByEmailWithRoles(@Param("email") String email);

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndId(String firstName,
                                     String lastName, Long id);

  boolean existsUserByEmail(String email);

  @Query("""
  SELECT DISTINCT f FROM User u
  JOIN u.friends f
  LEFT JOIN FETCH f.roles
  LEFT JOIN FETCH f.gender
  LEFT JOIN FETCH f.hometown
  LEFT JOIN FETCH f.currentCity
  WHERE u.email = :email
      """)
  List<User> findFriendsByUserEmail(@Param("email") String email);


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
