package pl.mateusz.example.friendoo.validation.age;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating user age.
 */
@Documented
@Constraint(validatedBy = UserAgeValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface UserAge {

  /**
   * The default message for the age validation.
   *
   * @return the default message
   */
  String message() default "{pl.mateusz.MinimumUserAge.message}";

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

  Class<? extends Payload>[] payload() default {};

  /**
   * The minimum age for the validation.
   *
   * @return the minimum age
   */
  int min() default 13;

  /**
   * The maximum age for the validation.
   *
   * @return the maximum age
   */
  int max() default 110;


}
