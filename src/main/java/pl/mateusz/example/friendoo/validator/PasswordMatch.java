package pl.mateusz.example.friendoo.validator;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface PasswordMatch {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  String message() default "{pl.mateusz.PasswordMatch.message}";
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<?>[] groups() default {};
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<? extends Payload>[] payload() default {};
}
