package pl.mateusz.example.friendoo.user;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserAdditionalDetailsDtoMapper {

  static UserAdditionalDetailsDto mapToUserAdditionalDetailsDto(User user) {
    String bio = user.getBio();
    String currentCity = user.getCurrentCity();
    String hometown = user.getHometown();
    String phoneNumber = user.getPhoneNumber();
    String email = user.getEmail();
    return new UserAdditionalDetailsDto(email, bio, currentCity, hometown, phoneNumber);
  }
}
