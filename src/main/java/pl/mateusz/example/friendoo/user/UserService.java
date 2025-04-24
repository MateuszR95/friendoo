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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.email.MailService;
import pl.mateusz.example.friendoo.exceptions.AccountAlreadyActivatedException;
import pl.mateusz.example.friendoo.exceptions.ExpiredActivationTokenException;
import pl.mateusz.example.friendoo.exceptions.InvalidTokenException;
import pl.mateusz.example.friendoo.exceptions.MailSendingException;
import pl.mateusz.example.friendoo.exceptions.UserLocationNotFoundException;
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
import pl.mateusz.example.friendoo.user.location.UserAddress;
import pl.mateusz.example.friendoo.user.location.UserAddressService;
import pl.mateusz.example.friendoo.user.location.UserLocationService;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetDto;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetDtoMapper;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetTokenService;
import pl.mateusz.example.friendoo.user.registration.UserRegistrationDto;
import pl.mateusz.example.friendoo.user.role.Role;
import pl.mateusz.example.friendoo.user.role.UserRole;
import pl.mateusz.example.friendoo.user.role.UserRoleRepository;

/**
 * Service for user operations.
 */
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

  private final UserLocationService userLocationService;

  private final UserAddressService userAddressService;


  private static final int SECONDS_30 = 30000;
  Logger logger = LoggerFactory.getLogger(UserService.class);

  /**
   * Constructs a UserService with the specified dependencies.
   */
  public UserService(UserRepository userRepository, UserGenderRepository userGenderRepository,
                     UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder,
                     MailService mailService, UserActivationTokenService userActivationTokenService,
                     UserPasswordResetTokenService userPasswordResetTokenService,
                     UserFavouritePageCategoryRepository userFavouritePageCategoryRepository,
                     PageCategoryRepository pageCategoryRepository,
                     UserLocationService userLocationService,
                     UserAddressService userAddressService) {
    this.userRepository = userRepository;
    this.userGenderRepository = userGenderRepository;
    this.userRoleRepository = userRoleRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailService = mailService;
    this.userActivationTokenService = userActivationTokenService;
    this.userPasswordResetTokenService = userPasswordResetTokenService;
    this.userFavouritePageCategoryRepository = userFavouritePageCategoryRepository;
    this.pageCategoryRepository = pageCategoryRepository;
    this.userLocationService = userLocationService;
    this.userAddressService = userAddressService;
  }

  /**
   * Finds a user by their email address and retrieves their credentials.
   *
   * @param email the email address of the user
   * @return an Optional containing the UserCredentialsDto if found, or empty if not found
   */
  public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
    return userRepository.findByEmailWithRoles(email)
      .map(UserCredentialsDtoMapper::mapToUserCredentialsDto);

  }

  public Optional<UserDisplayDto> findUserToDisplay(String email) {
    return userRepository.findUserByEmail(email)
      .map(UserDisplayDtoMapper::mapToUserDisplayDto);
  }

  /**
   * Retrieves a list of friends for the user with the specified email.
   *
   * @param email the email of the user
   * @return a list of UserDisplayDto representing the user's friends
   */
  public List<UserDisplayDto> getUserFriendsList(String email) {
    List<User> friendsByEmail = userRepository.findFriendsByUserEmail(email);
    return friendsByEmail.stream().map(UserDisplayDtoMapper::mapToUserDisplayDto)
      .collect(Collectors.toList());
  }

  /**
   * Registers a new user account.
   *
   * @param dto the data transfer object containing user registration details
   */
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

  /**
   * Activates a user account if the provided token is valid.
   *
   * @param userEmail the email of the user
   * @param enteredToken the activation token entered by the user
   * @throws ExpiredActivationTokenException if the activation token has expired
   * @throws UserNotFoundException if the user is not found
   * @throws AccountAlreadyActivatedException if the account is already activated
   * @throws InvalidTokenException if the provided token is invalid
   */
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

  /**
   * Deletes inactive users and their roles if they do not have a valid activation token.
   * This method is scheduled to run at a fixed rate.
   */
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

  /**
   * Checks if the user's account is activated.
   *
   * @param userEmail the email of the user
   * @return true if the account is activated, false otherwise
   * @throws UserNotFoundException if the user is not found
   */
  public Boolean isAccountActivated(String userEmail) {
    User user = userRepository.findUserByEmail(userEmail).orElseThrow(()
        -> new UserNotFoundException("Brak użytkownika o wskazanym adresie emailowym"));
    return user.isActiveAccount();
  }

  /**
   * Sends a password reset link to the user with the specified email.
   *
   * @param email the email of the user
   * @throws MailSendingException if the email cannot be sent
   */
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

  /**
   * Resets the user's password.
   *
   * @param userPasswordResetDto the data transfer object containing the new password
   * @return true if the password was reset, false otherwise
   */
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

  /**
   * Checks if the user's profile is completed.
   *
   * @param userEmail the email of the user
   * @return true if the profile is completed, false otherwise
   * @throws UserNotFoundException if the user is not found
   */
  public boolean isUserProfileCompleted(String userEmail) {
    User user = userRepository.findUserByEmail(userEmail).orElseThrow(()
        -> new UserNotFoundException("Brak użytkownika o wskazanym adresie emailowym"));
    return user.getBio() != null && user.getCurrentCity() != null && user.getHometown()
      != null && user.getPhoneNumber() != null;
  }

  /**
   * Retrieves the user's additional details to complete the profile.
   *
   * @param email the email of the user
   * @return a UserAdditionalDetailsDto representing the user's additional details
   * @throws UserNotFoundException if the user is not found
   */
  public UserAdditionalDetailsDto findUserToCompleteProfile(String email) {
    User user = userRepository.findUserByEmail(email).orElseThrow(() ->
      new UsernameNotFoundException("Brak użytkownika o wskazanym adresie emailowym"));
    return UserAdditionalDetailsDtoMapper.mapToUserAdditionalDetailsDto(user);
  }

  /**
   * Completes the user's profile details.
   *
   * @param userAdditionalDetailsDto the data transfer object containing the user's
   *                                additional details
   */
  @Transactional
  public void completeUserProfileDetails(UserAdditionalDetailsDto userAdditionalDetailsDto) {
    User user = getUserByEmail(userAdditionalDetailsDto);
    setBasicUserInfo(userAdditionalDetailsDto, user);
    setUserAddresses(userAdditionalDetailsDto, user);
    setUserFavouriteCategories(userAdditionalDetailsDto, user);
    userRepository.save(user);
  }

  private void setUserFavouriteCategories(UserAdditionalDetailsDto userAdditionalDetailsDto,
                                          User user) {
    Set<Long> favouritePageCategoriesIds = userAdditionalDetailsDto.getFavouritePageCategoriesIds();
    List<PageCategory> pageCategories = pageCategoryRepository
        .findAllById(favouritePageCategoriesIds);
    for (PageCategory pageCategory : pageCategories) {
      UserFavouritePageCategory userFavouritePageCategory = new UserFavouritePageCategory();
      userFavouritePageCategory.setUser(user);
      userFavouritePageCategory.setPageCategory(pageCategory);
      userFavouritePageCategoryRepository.save(userFavouritePageCategory);
    }
  }

  private void setUserAddresses(UserAdditionalDetailsDto userAdditionalDetailsDto, User user) {
    UserAddress currentCity = userLocationService.chooseLocation(userAdditionalDetailsDto
        .getCurrentCity()).orElseThrow(() -> new UserLocationNotFoundException(
          "Nie znaleziono lokalizacji"));
    UserAddress hometown = userLocationService.chooseLocation(userAdditionalDetailsDto
        .getHometown()).orElseThrow(() ->
         new UserLocationNotFoundException("Nie znaleziono lokalizacji"));
    userAddressService.assignAddressesToUser(currentCity, user, hometown);
  }

  private static void setBasicUserInfo(UserAdditionalDetailsDto userAdditionalDetailsDto,
                                          User user) {
    user.setBio(userAdditionalDetailsDto.getBio());
    user.setPhoneNumber(userAdditionalDetailsDto.getPhoneNumber());
  }

  private User getUserByEmail(UserAdditionalDetailsDto userAdditionalDetailsDto) {
    return userRepository.findUserByEmail(userAdditionalDetailsDto.getEmail())
        .orElseThrow(() ->
        new UsernameNotFoundException("Brak użytkownika o wskazanym adresie emailowym"));
  }

  /**
   * Finds a user by their ID.
   *
   * @param id the ID of the user
   * @return an Optional containing the UserDisplayDto if found, or empty if not found
   */
  public Optional<UserDisplayDto> findUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("Brak użytkownika o wskazanym id"));
    return Optional.of(UserDisplayDtoMapper.mapToUserDisplayDto(user));
  }

  public Optional<UserDisplayDto> findUserByNameAndId(String firstName, String lastName, Long id) {
    return userRepository.findUserByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndId(firstName,
      lastName, id).map(UserDisplayDtoMapper::mapToUserDisplayDto);
  }

  /**
   * Retrieves the IDs of the current logged-in user's friends.
   *
   * @param authentication the authentication object containing user details
   * @return a list of friend IDs
   */
  public List<Long> getCurrentLoggedUserFriendsIds(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return getUserFriendsList(userDetails
        .getUsername())
      .stream()
      .map(UserDisplayDto::getId)
      .toList();
  }

}

