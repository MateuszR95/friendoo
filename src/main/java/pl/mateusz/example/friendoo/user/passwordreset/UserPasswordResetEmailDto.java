package pl.mateusz.example.friendoo.user.passwordreset;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.validation.FirstOrder;
import pl.mateusz.example.friendoo.validation.SecondOrder;
import pl.mateusz.example.friendoo.validation.ThirdOrder;
import pl.mateusz.example.friendoo.validation.email.EmailExists;
import pl.mateusz.example.friendoo.validation.pattern.ValidationPatterns;

/**
 * Data transfer object for user password reset email.
 */
@Data
@NoArgsConstructor
@GroupSequence({UserPasswordResetEmailDto.class, FirstOrder.class, SecondOrder.class,
  ThirdOrder.class})
public class UserPasswordResetEmailDto {

  @NotBlank(groups = FirstOrder.class)
  @Pattern(groups = SecondOrder.class, regexp = ValidationPatterns.EMAIL_REGEX_PATTERN,
      message = "Niewłaściwy format adresu email")
  @EmailExists(groups = ThirdOrder.class)
  private String email;

  /**
   * Sets the email address.
   *
   * @param email the email address to set
   */
  public void setEmail(String email) {
    if (email != null) {
      this.email = email.trim();
    } else {
      this.email = null;
    }
  }

}
