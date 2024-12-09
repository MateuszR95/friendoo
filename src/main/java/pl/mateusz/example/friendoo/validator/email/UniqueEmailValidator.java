package pl.mateusz.example.friendoo.validator.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mateusz.example.friendoo.user.UserRepository;

@SuppressWarnings("checkstyle:MissingJavadocType")
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
    return !userRepository.existsUserByEmail(email);
  }

}
