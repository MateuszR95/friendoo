package pl.mateusz.example.friendoo.validator.email;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Documented
@Constraint(validatedBy = EmailExistValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface EmailExists {
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  String message() default "{pl.mateusz.EmailExists.message}";
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<?>[] groups() default {};
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<? extends Payload>[] payload() default {};
}


