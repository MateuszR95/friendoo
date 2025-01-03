package pl.mateusz.example.friendoo.user.passwordreset;

import jakarta.validation.Valid;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mateusz.example.friendoo.exceptions.MailSendingException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.user.UserService;
import pl.mateusz.example.friendoo.user.registration.UserRegistrationController;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Controller
public class UserPasswordResetController {

  private final UserService userService;

  private final UserPasswordResetTokenService userPasswordResetTokenService;

  private final Logger logger = LoggerFactory.getLogger(UserPasswordResetController.class);

  public UserPasswordResetController(UserService userService,
                                     UserPasswordResetTokenService userPasswordResetTokenService) {
    this.userService = userService;
    this.userPasswordResetTokenService = userPasswordResetTokenService;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @GetMapping("/password-reset-email")
  public String displayPasswordResetEmailForm(Model model) {
    UserPasswordResetEmailDto userPasswordResetEmailDto = new UserPasswordResetEmailDto();
    model.addAttribute("userResetEmail", userPasswordResetEmailDto);
    return "password-reset-email-form";
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @PostMapping("/password-reset-email")
  public String passwordResetEmail(@Valid @ModelAttribute(name = "userResetEmail")
                                     UserPasswordResetEmailDto dto, BindingResult bindingResult,
                                      Model model) {
    if (bindingResult.hasErrors()) {
      return "password-reset-email-form";
    }
    try {
      userService.sendPasswordResetLink(dto.getEmail().toLowerCase().trim());
      logger.info("Email z linkiem do resetu hasła wysłany pomyślnie do {}", dto.getEmail());
      return "password-reset-email-success";
    } catch (UserNotFoundException e) {
      logger.error("Użytkownik a adresie email {} nie istnieje", dto.getEmail());
      model.addAttribute("errorMessage",
          "Konto z takim adresem email nie istnieje.");
      model.addAttribute("email", dto.getEmail());
      return "password-reset-email-form";
    } catch (MailSendingException e) {
      logger.error("Nie udało się wysłać emaila z linkiem do resetu hasła na adres {}",
          dto.getEmail());
      model.addAttribute("errorMessage",
          "Wystąpił problem z wysyłką wiadomości e-mail. Spróbuj ponownie później.");
      return "password-reset-email-form";
    }
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @GetMapping("/password-reset")
  public String displayPasswordResetForm(@RequestParam("key") String key, Model model) {
    userPasswordResetTokenService.invalidateExpiredAndUsedUserPasswordResetTokens();
    Optional<UserPasswordResetTokenDto> validUserPasswordResetTokenByTokenCode =
        userPasswordResetTokenService.getValidUserPasswordResetTokenByTokenCode(key);
    if (validUserPasswordResetTokenByTokenCode.isPresent()) {
      String userEmail = validUserPasswordResetTokenByTokenCode.get().getUserEmail();
      UserPasswordResetDto userPasswordResetDto = userService.getUserPasswordResetDtoByEmail(
          userEmail).orElseThrow(() -> new UserNotFoundException(
            "Brak użytkownika o wskazanym adresie emailowym"));
      model.addAttribute("userPasswordResetDto", userPasswordResetDto);
      return "password-reset-form";
    } else {
      return "error/404";
    }
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @PostMapping("/password-reset")
  public String resetPassword(@RequestParam("key") String key,
                              @Valid @ModelAttribute UserPasswordResetDto userPasswordResetDto,
                              BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "password-reset-form";
    }
    if (userService.resetPassword(userPasswordResetDto)) {
      logger.info("Hasło użytkownika {} zresetowane pomyślnie", userPasswordResetDto.getEmail());
      userPasswordResetTokenService.invalidateUsedUserPasswordResetTokenByCode(key);
      return "password-reset-success";
    }
    logger.error("Błąd podczas resetowania hasła dla użytkownika {}",
        userPasswordResetDto.getEmail());
    return "error/500";
  }

}
