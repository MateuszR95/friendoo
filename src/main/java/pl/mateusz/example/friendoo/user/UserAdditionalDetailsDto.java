package pl.mateusz.example.friendoo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.validator.location.ValidLocation;
import pl.mateusz.example.friendoo.validator.pattern.ValidationPatterns;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAdditionalDetailsDto {

  private String email;
  @NotBlank
  private String bio;
  @ValidLocation
  @NotBlank
  private String currentCity;
  @ValidLocation
  @NotBlank
  private String hometown;
  @NotBlank
  @Pattern(regexp = ValidationPatterns.USER_PHONE_NUMBER_PATTERN,
      message = "Niepoprawny format numeru telefonu."
        + " Użyj formatu międzynarodowego, np. +48123456789.")
  private String phoneNumber;

}
