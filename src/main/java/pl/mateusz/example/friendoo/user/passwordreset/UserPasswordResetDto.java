package pl.mateusz.example.friendoo.user.passwordreset;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.validation.FirstOrder;
import pl.mateusz.example.friendoo.validation.SecondOrder;
import pl.mateusz.example.friendoo.validation.ThirdOrder;
import pl.mateusz.example.friendoo.validation.email.EmailExists;
import pl.mateusz.example.friendoo.validation.password.PasswordMatch;
import pl.mateusz.example.friendoo.validation.password.PasswordMatchable;
import pl.mateusz.example.friendoo.validation.pattern.ValidationPatterns;

/**
 * Data transfer object for user password reset.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({UserPasswordResetDto.class, FirstOrder.class, SecondOrder.class,
  ThirdOrder.class})
@PasswordMatch(groups = ThirdOrder.class)
public class UserPasswordResetDto implements PasswordMatchable {

  @EmailExists
  private String email;

  @NotBlank(groups = FirstOrder.class)
  @Pattern(regexp = ValidationPatterns.PASSWORD_REGEX_PATTERN,
        message = "Hasło musi zawierać przynajmniej 1 dużą "
      + "literę, 1 małą literę, 1 znak specjalny oraz mieć co najmniej 8 znaków",
      groups = SecondOrder.class)
  private String password;
  @NotBlank(groups = FirstOrder.class)
  private String repeatedPassword;


  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getRepeatedPassword() {
    return repeatedPassword;
  }
}
