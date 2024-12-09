package pl.mateusz.example.friendoo.user.registration;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mateusz.example.friendoo.exceptions.AccountAlreadyActivatedException;
import pl.mateusz.example.friendoo.exceptions.InvalidTokenException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserValidationException;
import pl.mateusz.example.friendoo.user.UserDisplayDto;
import pl.mateusz.example.friendoo.user.UserService;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Controller
public class UserRegistrationController {

  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

  public UserRegistrationController(UserService userService) {
    this.userService = userService;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @GetMapping("/registration")
  public String displayRegistrationForm(Model model) {
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    model.addAttribute("userRegistrationDto", userRegistrationDto);
    return "registration-form";
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @PostMapping("/registration")
  public String register(@Valid @ModelAttribute UserRegistrationDto userRegistrationDto,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes,
                         Model model, HttpSession session) {
    if (bindingResult.hasErrors()) {
      return "registration-form";
    }
    try {
      userService.registerAccount(userRegistrationDto);
      redirectAttributes.addFlashAttribute("userEmail", userRegistrationDto.getEmail());
      session.setAttribute("isActivationAllowed", true);
      return "redirect:/activation";
    } catch (MailSendException | UserValidationException e) {
      logger.info(e.getMessage());
      model.addAttribute("errorMessage", "Nie udało się wysłać tokenu aktywacyjnego,"
          + " sprawdź poprawność podanego adresu e-mail");
      return "registration-form";
    }
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @GetMapping("/activation")
  public String displayAccountActivationForm(@ModelAttribute(value = "userEmail") String userEmail,
                                             Model model, HttpSession session) {
    Boolean isActivationAllowed = (Boolean) session.getAttribute("isActivationAllowed");
    if (isActivationAllowed == null || !isActivationAllowed) {
      return "redirect:/";
    }
    session.removeAttribute("isActivationAllowed");
    model.addAttribute("userEmail", userEmail);
    return "registration-activation";
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @PostMapping("/activation")
  public String activateAccount(@RequestParam String enteredToken,
                                @RequestParam String userEmail, Model model) {
    try {
      userService.activateAccount(userEmail, enteredToken);
      Optional<UserDisplayDto> userToDisplay = userService.findUserToDisplay(userEmail);
      userToDisplay.ifPresent(userDisplayDto
          -> model.addAttribute("userName", userDisplayDto.getFirstName()));
      return "activation-success";
    } catch (InvalidTokenException | UserNotFoundException | AccountAlreadyActivatedException e) {
      model.addAttribute("userEmail", userEmail);
      model.addAttribute("errorMessage", e.getMessage());
      return "registration-activation";
    }
  }
}
