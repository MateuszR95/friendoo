package pl.mateusz.example.friendoo.user.passwordreset;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for user password reset token.
 */
@Data
@NoArgsConstructor
public class UserPasswordResetTokenDto {

  private String userEmail;

  private String tokenCode;
}
