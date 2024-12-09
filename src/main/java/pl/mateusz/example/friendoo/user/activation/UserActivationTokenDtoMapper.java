package pl.mateusz.example.friendoo.user.activation;


@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserActivationTokenDtoMapper {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public static UserActivationTokenDto mapToUserActivationTokenDto(UserActivationToken
                                                                       userActivationToken) {
    UserActivationTokenDto userActivationTokenDto = new UserActivationTokenDto();
    userActivationTokenDto.setUserEmail(userActivationToken.getUser().getEmail());
    userActivationTokenDto.setToken(userActivationToken.getToken());
    return userActivationTokenDto;
  }
}
