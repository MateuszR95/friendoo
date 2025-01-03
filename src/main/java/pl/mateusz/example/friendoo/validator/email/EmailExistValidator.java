package pl.mateusz.example.friendoo.validator.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mateusz.example.friendoo.user.UserRepository;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class EmailExistValidator implements ConstraintValidator<EmailExists, String> {

  private final UserRepository userRepository;

  @Autowired
  public EmailExistValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void initialize(EmailExists constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    return userRepository.existsUserByEmail(email.toLowerCase().trim());
  }
}
