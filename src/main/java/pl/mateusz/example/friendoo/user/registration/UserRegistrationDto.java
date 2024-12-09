package pl.mateusz.example.friendoo.user.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import pl.mateusz.example.friendoo.validator.PasswordMatch;
import pl.mateusz.example.friendoo.validator.UserAge;
import pl.mateusz.example.friendoo.validator.email.UniqueEmail;

@PasswordMatch
@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
public class UserRegistrationDto {

  private static final String EMAIL_REGEX_PATTERN =
      "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)"
        + "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
  private static final String PASSWORD_REGEX_PATTERN = "^(?=.*[A-Z])"
      + "(?=.*[a-z])(?=.*[#?!@$%^&*-]).+$";

  @NotBlank
  @Size(min = 2)
  private String firstName;
  @NotBlank
  @Size(min = 2)
  private String lastName;
  @NotBlank
  @Email
  @Pattern(regexp = EMAIL_REGEX_PATTERN,
      message = "Niewłaściwy format adresu email")
  @UniqueEmail
  private String email;
  @NotNull
  @UserAge
  private LocalDate dateOfBirth;
  @NotBlank
  private String gender;
  @NotBlank
  @Pattern(regexp = PASSWORD_REGEX_PATTERN, message = "Hasło musi zawierać przynajmniej 1 dużą "
      + "literę, 1 małą literę, 1 znak specjalny oraz mieć co najmniej 8 znaków")
  private String password;
  @NotBlank
  private String repeatedPassword;


}
