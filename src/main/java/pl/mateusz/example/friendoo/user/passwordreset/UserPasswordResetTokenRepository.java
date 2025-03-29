package pl.mateusz.example.friendoo.user.passwordreset;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.example.friendoo.user.User;


/**
 * Repository for user password reset token.
 */
public interface UserPasswordResetTokenRepository extends JpaRepository<UserPasswordResetToken,
    Long> {

  Optional<UserPasswordResetToken>
      findFirstByUserEmailAndIsUsedFalseAndExpireDateIsAfterOrderByCreationDateDesc(
        String userEmail, LocalDateTime localDateTime);

  List<UserPasswordResetToken> getAllByIsValidTrue();

  List<UserPasswordResetToken> getAllByUserAndIsValidTrue(User user);

  Optional<UserPasswordResetToken> getByTokenAndIsValidTrue(String tokenCode);

  Optional<UserPasswordResetToken> getByToken(String tokenCode);

}
