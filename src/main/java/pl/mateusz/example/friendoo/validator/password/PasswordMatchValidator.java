package pl.mateusz.example.friendoo.validator.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.mateusz.example.friendoo.validator.password.PasswordMatch;
import pl.mateusz.example.friendoo.validator.password.PasswordMatchable;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch,
    PasswordMatchable> {

  @Override
  public void initialize(PasswordMatch constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(PasswordMatchable value,
                         ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) {
      return false;
    }
    return value.getPassword() != null && value.getPassword().equals(value.getRepeatedPassword());
  }

}
