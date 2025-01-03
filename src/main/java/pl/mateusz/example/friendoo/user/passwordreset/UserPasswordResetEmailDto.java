package pl.mateusz.example.friendoo.user.passwordreset;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.validator.FirstOrder;
import pl.mateusz.example.friendoo.validator.SecondOrder;
import pl.mateusz.example.friendoo.validator.ThirdOrder;
import pl.mateusz.example.friendoo.validator.email.EmailExists;
import pl.mateusz.example.friendoo.validator.pattern.ValidationPatterns;

@SuppressWarnings("checkstyle:MissingJavadocType")
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

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public void setEmail(String email) {
    if (email != null) {
      this.email = email.trim();
    } else {
      this.email = null;
    }
  }
}
