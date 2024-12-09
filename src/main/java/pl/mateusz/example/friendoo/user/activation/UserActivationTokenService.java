package pl.mateusz.example.friendoo.user.activation;

import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.user.User;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class UserActivationTokenService {

  private final UserActivationTokenRepository userActivationTokenRepository;

  private static final int EXPIRATION_TIME_IN_MINUTES = 15;

  private static final int TOKEN_LENGTH = 8;

  private static final int SECONDS_30 = 30000;

  public UserActivationTokenService(UserActivationTokenRepository userActivationTokenRepository) {
    this.userActivationTokenRepository = userActivationTokenRepository;
  }

  @Transactional
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public UserActivationToken generateAndSaveToken(User user) {
    UserActivationToken userActivationToken = new UserActivationToken();
    userActivationToken.setUser(user);
    String tokenCode = RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH);
    userActivationToken.setToken(tokenCode);
    userActivationToken.setCreationDate(user.getJoinedAt());
    userActivationToken.setExpireDate(user.getJoinedAt().plusMinutes(EXPIRATION_TIME_IN_MINUTES));
    return userActivationTokenRepository.save(userActivationToken);
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Transactional
  @Scheduled(fixedRate = SECONDS_30)
  public void deleteExpiredTokens() {
    userActivationTokenRepository.deleteExpiredTokens(LocalDateTime.now());
  }


  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public Optional<UserActivationTokenDto> getUserActivationTokenByTokenCode(String token) {
    Optional<UserActivationToken> userActivationTokenByToken =
        userActivationTokenRepository.getUserActivationTokenByToken(token);
    return userActivationTokenByToken
      .map(UserActivationTokenDtoMapper::mapToUserActivationTokenDto);
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public Optional<UserActivationTokenDto> getUserActivationTokenByUserEmail(String email) {
    Optional<UserActivationToken> userActivationTokenByUserEmail = userActivationTokenRepository
        .getUserActivationTokenByUserEmail(email);
    return userActivationTokenByUserEmail.map((UserActivationTokenDtoMapper
      ::mapToUserActivationTokenDto));
  }

  @Transactional
  public void deleteTokenByUserEmail(String userEmail) {
    userActivationTokenRepository.deleteByUserEmail(userEmail);
  }


}
