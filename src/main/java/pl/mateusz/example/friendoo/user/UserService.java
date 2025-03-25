package pl.mateusz.example.friendoo.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.email.MailService;
import pl.mateusz.example.friendoo.exceptions.AccountAlreadyActivatedException;
import pl.mateusz.example.friendoo.exceptions.ExpiredActivationTokenException;
import pl.mateusz.example.friendoo.exceptions.InvalidTokenException;
import pl.mateusz.example.friendoo.exceptions.MailSendingException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserValidationException;
import pl.mateusz.example.friendoo.gender.Gender;
import pl.mateusz.example.friendoo.gender.UserGender;
import pl.mateusz.example.friendoo.gender.UserGenderRepository;
import pl.mateusz.example.friendoo.page.category.PageCategory;
import pl.mateusz.example.friendoo.page.category.PageCategoryRepository;
import pl.mateusz.example.friendoo.user.activation.UserActivationToken;
import pl.mateusz.example.friendoo.user.activation.UserActivationTokenDto;
import pl.mateusz.example.friendoo.user.activation.UserActivationTokenService;
import pl.mateusz.example.friendoo.user.favouritepagecategory.UserFavouritePageCategory;
import pl.mateusz.example.friendoo.user.favouritepagecategory.UserFavouritePageCategoryRepository;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetDto;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetDtoMapper;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetTokenService;
import pl.mateusz.example.friendoo.user.registration.UserRegistrationDto;
import pl.mateusz.example.friendoo.user.role.Role;
import pl.mateusz.example.friendoo.user.role.UserRole;
import pl.mateusz.example.friendoo.user.role.UserRoleRepository;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserGenderRepository userGenderRepository;
  private final UserRoleRepository userRoleRepository;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;
  private final UserActivationTokenService userActivationTokenService;

  private final UserPasswordResetTokenService userPasswordResetTokenService;

  private final UserFavouritePageCategoryRepository userFavouritePageCategoryRepository;

  private final PageCategoryRepository pageCategoryRepository;

  private static final int SECONDS_30 = 30000;
  Logger logger = LoggerFactory.getLogger(UserService.class);

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public UserService(UserRepository userRepository, UserGenderRepository userGenderRepository,
                     UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder,
                     MailService mailService, UserActivationTokenService userActivationTokenService,
                     UserPasswordResetTokenService userPasswordResetTokenService,
                     UserFavouritePageCategoryRepository userFavouritePageCategoryRepository,
                     PageCategoryRepository pageCategoryRepository) {
    this.userRepository = userRepository;
    this.userGenderRepository = userGenderRepository;
    this.userRoleRepository = userRoleRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailService = mailService;
    this.userActivationTokenService = userActivationTokenService;
    this.userPasswordResetTokenService = userPasswordResetTokenService;
    this.userFavouritePageCategoryRepository = userFavouritePageCategoryRepository;
    this.pageCategoryRepository = pageCategoryRepository;
  }

  public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
    return userRepository.findUserByEmail(email)
      .map(UserCredentialsDtoMapper::mapToUserCredentialsDto);
  }

  public Optional<UserDisplayDto> findUserToDisplay(String email) {
    return userRepository.findUserByEmail(email)
      .map(UserCredentialsDtoMapper::mapToUserDisplayDto);
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public List<UserDisplayDto> getUserFriendsList(String email) {
    List<User> friendsByEmail = userRepository.findFriendsByEmail(email);
    return friendsByEmail.stream().map(UserCredentialsDtoMapper::mapToUserDisplayDto)
      .collect(Collectors.toList());
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Transactional
  public void registerAccount(UserRegistrationDto dto) {
    validateUserExistence(dto);
    User user = createUserFromDto(dto);
    userRepository.save(user);
    UserActivationToken userActivationToken = userActivationTokenService.generateAndSaveToken(user);
    boolean isActivationCodeSent = mailService.sendActivationCode(user.getEmail(),
        user.getFirstName(), userActivationToken.getToken());
    if (!isActivationCodeSent) {
      logger.warn("Nie udało się wysłać e-maila aktywacyjnego do {}", user.getEmail());
      throw new MailSendException("Nie udało się wysłać tokenu aktywacyjnego, sprawdź poprawność"
        + " podanego adresu e-mail");
    }
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Transactional
  public void activateAccount(String userEmail, String enteredToken) {
    UserActivationTokenDto userActivationTokenDto = userActivationTokenService
        .getUserActivationTokenByUserEmail(userEmail).orElseThrow(()
            -> new ExpiredActivationTokenException("Twój kod aktywacyjny wygasł"));
    if (userActivationTokenDto.getToken().equals(enteredToken)) {
      User user = userRepository.findUserByEmail(userEmail).orElseThrow(()
          -> new UserNotFoundException("Nie znaleziono użytkownika"));
      if (user.isActiveAccount()) {
        throw new AccountAlreadyActivatedException("Konto już zostało aktywowane.");
      }
      user.setActiveAccount(true);
      userRepository.save(user);
      userActivationTokenService.deleteTokenByUserEmail(user.getEmail());
    } else {
      throw new InvalidTokenException("Wprowadzony kod aktywacyjny jest niepoprawny");
    }
  }


  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Transactional
  @Scheduled(fixedRate = SECONDS_30)
  protected void deleteInactiveUserAndRolesWithoutValidToken() {
    userRepository.deleteRolesForInactiveUsersWithoutValidActivationToken();
    userRepository.deleteInactiveUsersWithoutValidActivationToken();

  }

  private User createUserFromDto(UserRegistrationDto dto) {
    User user = new User();
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setEmail(dto.getEmail().toLowerCase());
    user.setDateOfBirth(dto.getDateOfBirth());
    UserGender userGender = validateUserGenderExistence(dto);
    user.setGender(userGender);
    if (isPasswordRepeatedCorrectly(dto.getPassword(), dto.getRepeatedPassword())) {
      String encryptedPassword = passwordEncoder.encode(dto.getPassword());
      user.setPassword(encryptedPassword);
    } else {
      throw new UserValidationException("Hasła nie są jednakowe");
    }
    UserRole role = validateUserRoleExistence();
    user.getRoles().add(role);
    user.setJoinedAt(LocalDateTime.now());
    user.setActiveAccount(false);
    return user;
  }

  private UserRole validateUserRoleExistence() {
    return userRoleRepository.getUserRoleByRole(Role.USER)
        .orElseThrow(() -> new UserValidationException("Brak wskazanej roli użytkownika"));
  }

  private void validateUserExistence(UserRegistrationDto dto) {
    boolean existsUserByEmail = userRepository.existsUserByEmail(dto.getEmail());
    if (existsUserByEmail) {
      throw new UserValidationException("Użytkownik o podanym adresie email już istnieje");
    }
  }

  private UserGender validateUserGenderExistence(UserRegistrationDto dto) {
    String genderName = dto.getGender();
    Gender gender = Gender.valueOf(genderName);
    return userGenderRepository.getUserGenderByGender(gender).orElseThrow(() ->
        new UserValidationException("Wybrano nieprawidłową płeć"));
  }

  private boolean isPasswordRepeatedCorrectly(String password, String repeatedPassword) {
    return password.equals(repeatedPassword);
  }


  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public Boolean isAccountActivated(String userEmail) {
    User user = userRepository.findUserByEmail(userEmail).orElseThrow(()
        -> new UserNotFoundException("Brak użytkownika o wskazanym adresie emailowym"));
    return user.isActiveAccount();
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Transactional
  public void sendPasswordResetLink(String email) {
    userPasswordResetTokenService.createAndSaveUserPasswordResetToken(email);
    boolean passwordResetEmailSent = mailService.sendPasswordResetEmail(email);
    if (!passwordResetEmailSent) {
      throw new MailSendingException("Nie udało się wysłać wiadomości do " + email);
    }
  }

  public Optional<UserPasswordResetDto> getUserPasswordResetDtoByEmail(String email) {
    return userRepository.findUserByEmail(email).map(UserPasswordResetDtoMapper
      ::mapToUserPasswordResetDto);
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Transactional
  public boolean resetPassword(UserPasswordResetDto userPasswordResetDto) {
    User user = userRepository.findUserByEmail(userPasswordResetDto.getEmail()).orElseThrow(() ->
      new UsernameNotFoundException("Brak użytkownika o wskazanym adresie mailowym"));
    if (userPasswordResetDto.getPassword().equals(
          userPasswordResetDto.getRepeatedPassword())) {
      user.setPassword(passwordEncoder.encode(userPasswordResetDto.getPassword()));
      userRepository.save(user);
      return true;
    }
    return false;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public boolean isUserProfileCompleted(String userEmail) {
    User user = userRepository.findUserByEmail(userEmail).orElseThrow(()
        -> new UserNotFoundException("Brak użytkownika o wskazanym adresie emailowym"));
    return user.getBio() != null && user.getCurrentCity() != null && user.getHometown()
      != null && user.getPhoneNumber() != null;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public UserAdditionalDetailsDto findUserToCompleteProfile(String email) {
    User user = userRepository.findUserByEmail(email).orElseThrow(() ->
      new UsernameNotFoundException("Brak użytkownika o wskazanym adresie emailowym"));
    return UserAdditionalDetailsDtoMapper.mapToUserAdditionalDetailsDto(user);
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Transactional
  public void completeUserProfileDetails(UserAdditionalDetailsDto userAdditionalDetailsDto) {
    User user = userRepository.findUserByEmail(userAdditionalDetailsDto.getEmail())
        .orElseThrow(() ->
        new UsernameNotFoundException("Brak użytkownika o wskazanym adresie emailowym"));
    user.setBio(userAdditionalDetailsDto.getBio());
    user.setCurrentCity(userAdditionalDetailsDto.getCurrentCity());
    user.setHometown(userAdditionalDetailsDto.getHometown());
    user.setPhoneNumber(userAdditionalDetailsDto.getPhoneNumber());
    Set<Long> favouritePageCategoriesIds = userAdditionalDetailsDto.getFavouritePageCategoriesIds();
    List<PageCategory> pageCategories = pageCategoryRepository
        .findAllById(favouritePageCategoriesIds);
    for (PageCategory pageCategory : pageCategories) {
      UserFavouritePageCategory userFavouritePageCategory = new UserFavouritePageCategory();
      userFavouritePageCategory.setUser(user);
      userFavouritePageCategory.setPageCategory(pageCategory);
      userFavouritePageCategoryRepository.save(userFavouritePageCategory);
    }
    userRepository.save(user);
  }
}

