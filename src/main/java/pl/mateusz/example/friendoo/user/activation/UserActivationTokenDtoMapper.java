package pl.mateusz.example.friendoo.user.activation;

/**
 * Mapper class for mapping {@link UserActivationToken} to {@link UserActivationTokenDto}.
 */
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
