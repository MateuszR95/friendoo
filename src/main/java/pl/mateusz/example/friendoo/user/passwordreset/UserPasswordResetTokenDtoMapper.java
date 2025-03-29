package pl.mateusz.example.friendoo.user.passwordreset;

/**
 * Mapper for {@link UserPasswordResetTokenDto}.
 */
public class UserPasswordResetTokenDtoMapper {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public static UserPasswordResetTokenDto mapToDto(UserPasswordResetToken userPasswordResetToken) {
    UserPasswordResetTokenDto userPasswordResetTokenDto = new UserPasswordResetTokenDto();
    userPasswordResetTokenDto.setTokenCode(userPasswordResetToken.getToken());
    userPasswordResetTokenDto.setUserEmail(userPasswordResetToken.getUser().getEmail());
    return userPasswordResetTokenDto;
  }
}
