package pl.mateusz.example.friendoo.validation.location;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating location.
 */
@Documented
@Constraint(validatedBy = LocationValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidLocation {

  /**
   * The default message for the location validation.
   *
   * @return the default message
   */
  String message() default "{pl.mateusz.LocationMatch.message}";

  /**
   * The groups the constraint belongs to.
   *
   * @return the groups
   */
  Class<?>[] groups() default {};

  /**
   * The payload associated with the constraint.
   *
   * @return the payload
   */
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<? extends Payload>[] payload() default {};
}

