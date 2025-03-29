package pl.mateusz.example.friendoo.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for checking if the password and repeated password match.
 */
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
