package pl.mateusz.example.friendoo.user.passwordreset;

import pl.mateusz.example.friendoo.user.User;

/**
 * Mapper for {@link UserPasswordResetDto}.
 */
public class UserPasswordResetDtoMapper {

  /**
   * Maps a User entity to a UserPasswordResetDto.
   *
   * @param user the User entity to map
   * @return the mapped UserPasswordResetDto
   */
  public static UserPasswordResetDto mapToUserPasswordResetDto(User user) {
    UserPasswordResetDto userPasswordResetDto = new UserPasswordResetDto();
    userPasswordResetDto.setEmail(user.getEmail());
    return userPasswordResetDto;
  }

}
