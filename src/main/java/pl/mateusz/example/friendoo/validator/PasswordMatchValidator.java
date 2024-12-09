package pl.mateusz.example.friendoo.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.mateusz.example.friendoo.user.registration.UserRegistrationDto;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch,
    UserRegistrationDto> {

  @Override
  public void initialize(PasswordMatch constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(UserRegistrationDto dto,
                         ConstraintValidatorContext constraintValidatorContext) {
    if (dto == null) {
      return false;
    }
    return dto.getPassword().equals(dto.getRepeatedPassword());

  }
}
