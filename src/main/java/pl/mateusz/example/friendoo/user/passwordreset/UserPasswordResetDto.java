package pl.mateusz.example.friendoo.user.passwordreset;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.validator.FirstOrder;
import pl.mateusz.example.friendoo.validator.PasswordMatch;
import pl.mateusz.example.friendoo.validator.PasswordMatchable;
import pl.mateusz.example.friendoo.validator.SecondOrder;
import pl.mateusz.example.friendoo.validator.ThirdOrder;
import pl.mateusz.example.friendoo.validator.email.EmailExists;
import pl.mateusz.example.friendoo.validator.pattern.ValidationPatterns;

@SuppressWarnings("checkstyle:MissingJavadocType")

@Data
@NoArgsConstructor
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
