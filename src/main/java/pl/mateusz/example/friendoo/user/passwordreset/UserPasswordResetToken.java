package pl.mateusz.example.friendoo.user.passwordreset;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserToken;

/**
 * Entity representing a user password reset token.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPasswordResetToken extends UserToken {

  private boolean isUsed;

  private boolean isValid;

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  private LocalDateTime usedDate;

  public void updateValidity(LocalDateTime now) {
    isValid = !isUsed && getExpireDate().isAfter(now);
  }
}
