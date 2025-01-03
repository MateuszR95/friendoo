package pl.mateusz.example.friendoo.user.passwordreset;

import pl.mateusz.example.friendoo.user.User;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserPasswordResetDtoMapper {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public static UserPasswordResetDto mapToUserPasswordResetDto(User user) {
    UserPasswordResetDto userPasswordResetDto = new UserPasswordResetDto();
    userPasswordResetDto.setEmail(user.getEmail());
    return userPasswordResetDto;
  }


}
