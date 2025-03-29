package pl.mateusz.example.friendoo.validation.email;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating email existence.
 */
@Documented
@Constraint(validatedBy = EmailExistValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface EmailExists {
  /**
   * The default message for the email existence validation.
   *
   * @return the default message
   */
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  String message() default "{pl.mateusz.EmailExists.message}";

  /**
   * The groups the constraint belongs to.
   *
   * @return the groups
   */
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<?>[] groups() default {};

  /**
   * The payload associated with the constraint.
   *
   * @return the payload
   */
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<? extends Payload>[] payload() default {};
}


