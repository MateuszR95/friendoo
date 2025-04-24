package pl.mateusz.example.friendoo.user;

import java.util.Set;
import java.util.stream.Collectors;
import pl.mateusz.example.friendoo.user.role.Role;
import pl.mateusz.example.friendoo.user.role.UserRole;

/**
 * Mapper for user credentials DTO.
 */
public class UserCredentialsDtoMapper {

  static UserCredentialsDto mapToUserCredentialsDto(User user) {
    Set<String> roles = user.getRoles()
        .stream()
        .map(UserRole::getRole)
        .map(Role::name)
        .collect(Collectors.toSet());
    return UserCredentialsDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .roles(roles)
        .isActiveAccount(user.isActiveAccount())
        .build();
  }

}
