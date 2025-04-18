package pl.mateusz.example.friendoo.user;

import java.time.LocalDate;

/**
 * Mapper class for converting User entities to UserDisplayDto.
 */
public class UserDisplayDtoMapper {

  static UserDisplayDto mapToUserDisplayDto(User user) {
    Long id = user.getId();
    String firstName = user.getFirstName();
    String lastName = user.getLastName();
    String phoneNumber = user.getPhoneNumber();
    String bio = user.getBio();
    String hometown = createLocationDescription(user.getHometown().getCity(),
        user.getHometown().getTown(), user.getHometown().getAdministrative(),
        user.getHometown().getVillage(), user.getHometown().getState(),
        user.getHometown().getCountry());
    String currentCity = createLocationDescription(user.getCurrentCity().getCity(),
        user.getCurrentCity().getTown(), user.getCurrentCity().getAdministrative(),
        user.getCurrentCity().getVillage(), user.getCurrentCity().getState(),
        user.getCurrentCity().getCountry());
    String country = user.getCurrentCity().getCountry();
    LocalDate dateOfBirth = user.getDateOfBirth();
    String gender = user.getGender().getGender().getPlName();
    return new UserDisplayDto(id, firstName, lastName, phoneNumber, bio, hometown, currentCity,
      country, dateOfBirth, gender);
  }

  private static String createLocationDescription(String city, String town, String administrative,
                                                  String village, String state, String country) {
    StringBuilder locationDescription = new StringBuilder();
    appendIfNotEmpty(locationDescription, city);
    appendIfNotEmpty(locationDescription, town);
    appendIfNotEmpty(locationDescription, administrative);
    appendIfNotEmpty(locationDescription, village);
    appendIfNotEmpty(locationDescription, state);
    appendIfNotEmpty(locationDescription, country);
    return !locationDescription.isEmpty() ? locationDescription.toString()
      : "Brak danych o lokalizacji";
  }

  private static void appendIfNotEmpty(StringBuilder builder, String value) {
    if (value != null && !value.isEmpty()) {
      if (!builder.isEmpty()) {
        builder.append(", ");
      }
      builder.append(value);
    }

  }
}
