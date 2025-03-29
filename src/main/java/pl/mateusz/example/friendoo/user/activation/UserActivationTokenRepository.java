package pl.mateusz.example.friendoo.user.activation;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for {@link UserActivationToken} entities.
 */
public interface UserActivationTokenRepository extends JpaRepository<UserActivationToken, Long> {

  Optional<UserActivationToken> getUserActivationTokenByUserEmail(String email);

  @Transactional
  @Modifying
  @Query("DELETE FROM UserActivationToken u WHERE u.expireDate < :now")
  void deleteExpiredTokens(@Param("now") LocalDateTime now);

  void deleteByUserEmail(String userEmail);

}
