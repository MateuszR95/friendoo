package pl.mateusz.example.friendoo.validation.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mateusz.example.friendoo.user.UserRepository;

/**
 * Validator for checking if the email is unique.
 */
@Service
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

  private UserRepository userRepository;

  @Autowired
  public UniqueEmailValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UniqueEmailValidator() {
  }

  @Override
  public void initialize(UniqueEmail constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    return !userRepository.existsUserByEmail(email.toLowerCase().trim());
  }

}
