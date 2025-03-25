package pl.mateusz.example.friendoo.validator.location;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Documented
@Constraint(validatedBy = LocationValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidLocation {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  String message() default "{pl.mateusz.LocationMatch.message}";
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<?>[] groups() default {};
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<? extends Payload>[] payload() default {};
}

