package pl.mateusz.example.friendoo.user.passwordreset;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserPasswordResetTokenDtoMapper {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public static UserPasswordResetTokenDto mapToDto(UserPasswordResetToken userPasswordResetToken) {
    UserPasswordResetTokenDto userPasswordResetTokenDto = new UserPasswordResetTokenDto();
    userPasswordResetTokenDto.setTokenCode(userPasswordResetToken.getToken());
    userPasswordResetTokenDto.setUserEmail(userPasswordResetToken.getUser().getEmail());
    return userPasswordResetTokenDto;
  }
}
