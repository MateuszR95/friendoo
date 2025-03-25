package pl.mateusz.example.friendoo.validator.age;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserAgeValidator implements ConstraintValidator<UserAge, LocalDate> {
  private static final int MINIMUM_AGE = 13;

  private static final int MAXIMUM_AGE = 110;

  private static final String MESSAGE_BELOW_MINIMUM_AGE = "Minimalny wiek do założenia "
      + "konta to 13 lat.";

  private static final String MESSAGE_ABOVE_MAXIMUM_AGE = "Podany wiek wydaje się nieprawidłowy."
      + " Upewnij się, że wprowadziłeś poprawną datę urodzenia.";

  @Override
  public void initialize(UserAge constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext
      constraintValidatorContext) {
    if (dateOfBirth == null) {
      return false;
    }
    int age = dateOfBirth.until(LocalDate.now()).getYears();

    if (age < MINIMUM_AGE) {

      constraintValidatorContext.disableDefaultConstraintViolation();
      constraintValidatorContext.buildConstraintViolationWithTemplate(
          MESSAGE_BELOW_MINIMUM_AGE).addConstraintViolation();
      return false;
    }

    if (age > MAXIMUM_AGE) {
      constraintValidatorContext.disableDefaultConstraintViolation();
      constraintValidatorContext.buildConstraintViolationWithTemplate(
          MESSAGE_ABOVE_MAXIMUM_AGE).addConstraintViolation();
      return false;
    }

    return true;
  }
}