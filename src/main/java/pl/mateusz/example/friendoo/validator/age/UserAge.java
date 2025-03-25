package pl.mateusz.example.friendoo.validator.age;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Documented
@Constraint(validatedBy = UserAgeValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface UserAge {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  String message() default "{pl.mateusz.MinimumUserAge.message}";
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<?>[] groups() default {};
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<? extends Payload>[] payload() default {};
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  int min() default 13;
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  int max() default 110;


}
