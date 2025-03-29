package pl.mateusz.example.friendoo.validation.password;


import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating password match.
 */
@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface PasswordMatch {

  /**
   * The default message for the password match validation.
   *
   * @return the default message
   */
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  String message() default "{pl.mateusz.PasswordMatch.message}";

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
