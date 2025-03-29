package pl.mateusz.example.friendoo.user.passwordreset;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserPasswordResetTokenException;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserRepository;

/**
 * Service for user password reset token.
 */
@Service
public class UserPasswordResetTokenService {

  private final UserPasswordResetTokenRepository userPasswordResetTokenRepository;
  private final UserRepository userRepository;
  private static final int EXPIRATION_TIME_IN_MINUTES = 1440;

  public UserPasswordResetTokenService(UserPasswordResetTokenRepository
                                         userPasswordResetTokenRepository,
                                          UserRepository userRepository) {
    this.userPasswordResetTokenRepository = userPasswordResetTokenRepository;
    this.userRepository = userRepository;
  }

  /**
   * Creates and saves a new user password reset token for the specified email.
   *
   * @param email the email address of the user
   * @throws UserNotFoundException if no user is found with the specified email
   */
  @Transactional
  public void createAndSaveUserPasswordResetToken(String email) {
    User user = userRepository.findUserByEmail(email).orElseThrow(()
        -> new UserNotFoundException("Brak użytkownika o wskazanym adresie emailowym"));
    invalidateExpiredAndUsedUserPasswordResetTokens();
    UserPasswordResetToken userPasswordResetToken = new UserPasswordResetToken();
    userPasswordResetToken.setUser(user);
    userPasswordResetToken.setToken(UUID.randomUUID().toString());
    userPasswordResetToken.setCreationDate(LocalDateTime.now());
    userPasswordResetToken.setExpireDate(userPasswordResetToken.getCreationDate()
        .plusMinutes(EXPIRATION_TIME_IN_MINUTES));
    userPasswordResetToken.setUsed(false);
    userPasswordResetToken.setValid(true);
    invalidateValidUserPasswordResetTokens(user);
    userPasswordResetTokenRepository.save(userPasswordResetToken);
  }

  /**
   * Invalidates all valid user password reset tokens for the specified email.
   *
   * @param email the email address of the user
   */
  public UserPasswordResetToken getUserPasswordResetTokenByEmail(String email) {
    return userPasswordResetTokenRepository
      .findFirstByUserEmailAndIsUsedFalseAndExpireDateIsAfterOrderByCreationDateDesc(
        email, LocalDateTime.now())
      .orElseThrow(
        () -> new UserPasswordResetTokenException("Nie znaleziono tokenu dla użytkownika "
        + "o podanym emailu"));
  }

  @Transactional
  protected void invalidateValidUserPasswordResetTokens(User user) {
    List<UserPasswordResetToken> validUserPasswordResetTokens =
        userPasswordResetTokenRepository.getAllByUserAndIsValidTrue(user);
    validUserPasswordResetTokens
        .forEach(userPasswordResetToken -> userPasswordResetToken.setValid(false));
  }

  @Transactional
  protected void invalidateExpiredAndUsedUserPasswordResetTokens() {
    userPasswordResetTokenRepository.getAllByIsValidTrue()
        .forEach(userPasswordResetToken -> userPasswordResetToken
          .updateValidity(LocalDateTime.now()));
  }

  @Transactional
  protected void invalidateUsedUserPasswordResetTokenByCode(String tokenCode) {
    UserPasswordResetToken userPasswordResetToken = userPasswordResetTokenRepository
        .getByToken(tokenCode).orElseThrow(()
            -> new UserPasswordResetTokenException("Nie znaleziono tokenu dla użytkownika"));
    userPasswordResetToken.setUsedDate(LocalDateTime.now(Clock.systemUTC()));
    userPasswordResetToken.setUsed(true);
    userPasswordResetToken.setValid(false);
  }

  /**
   * Retrieves a valid user password reset token by its token code.
   *
   * @param tokenCode the token code
   * @return an Optional containing the UserPasswordResetTokenDto if found and valid, otherwise
   *      empty
   */
  @Transactional
  public Optional<UserPasswordResetTokenDto> getValidUserPasswordResetTokenByTokenCode(
      String tokenCode) {
    Optional<UserPasswordResetToken> byTokenAndIsValidTrue = userPasswordResetTokenRepository
        .getByTokenAndIsValidTrue(tokenCode);
    return byTokenAndIsValidTrue.map(UserPasswordResetTokenDtoMapper::mapToDto);
  }

}
