package pl.mateusz.example.friendoo.user;

import java.util.Set;
import java.util.stream.Collectors;
import pl.mateusz.example.friendoo.user.role.Role;
import pl.mateusz.example.friendoo.user.role.UserRole;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserDtoMapper {

  @SuppressWarnings("checkstyle:Indentation")
  static UserCredentialsDto mapToUserCredentialsDto(User user) {
    String email = user.getEmail();
    String password = user.getPassword();
    Set<String> roles = user.getRoles()
      .stream()
      .map(UserRole::getRole)
      .map(Role::name)
      .collect(Collectors.toSet());
    return new UserCredentialsDto(email, password, roles);
  }

  static UserDisplayDto mapToUserDisplayDto(User user) {
    Long id = user.getId();
    String firstName = user.getFirstName();
    String lastName = user.getLastName();
    return new UserDisplayDto(id, firstName, lastName);
  }
}
