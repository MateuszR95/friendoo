package pl.mateusz.example.friendoo.validation.location;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import pl.mateusz.example.friendoo.user.location.UserLocationService;

/**
 * Validator for checking if the location is valid.
 */
public class LocationValidator implements ConstraintValidator<ValidLocation, String> {

  private final UserLocationService userLocationService;

  public LocationValidator(UserLocationService userLocationService) {
    this.userLocationService = userLocationService;
  }

  @Override
  public void initialize(ValidLocation constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String location, ConstraintValidatorContext constraintValidatorContext) {
    List<String> locations = userLocationService.getLocations(location);
    return locations.stream()
      .anyMatch(query -> query.equalsIgnoreCase(location));
  }

}
