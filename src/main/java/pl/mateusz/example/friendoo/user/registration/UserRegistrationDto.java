package pl.mateusz.example.friendoo.user.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.validator.age.UserAge;
import pl.mateusz.example.friendoo.validator.email.UniqueEmail;
import pl.mateusz.example.friendoo.validator.password.PasswordMatch;
import pl.mateusz.example.friendoo.validator.password.PasswordMatchable;
import pl.mateusz.example.friendoo.validator.pattern.ValidationPatterns;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
@NoArgsConstructor
@PasswordMatch
@AllArgsConstructor
@Builder
public class UserRegistrationDto implements PasswordMatchable {

  @NotBlank
  @Size(min = 2)
  private String firstName;
  @NotBlank
  @Size(min = 2)
  private String lastName;
  @NotBlank
  @Email
  @Pattern(regexp = ValidationPatterns.EMAIL_REGEX_PATTERN,
      message = "Niewłaściwy format adresu email")
  @UniqueEmail
  private String email;
  @NotNull
  @UserAge
  private LocalDate dateOfBirth;
  @NotBlank
  private String gender;
  @NotBlank
  @Pattern(regexp = ValidationPatterns.PASSWORD_REGEX_PATTERN,
        message = "Hasło musi zawierać przynajmniej 1 dużą "
      + "literę, 1 małą literę, 1 znak specjalny oraz mieć co najmniej 8 znaków")
  private String password;
  @NotBlank
  private String repeatedPassword;


}
