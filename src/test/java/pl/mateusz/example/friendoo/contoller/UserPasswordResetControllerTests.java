package pl.mateusz.example.friendoo.contoller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.mateusz.example.friendoo.exceptions.MailSendingException;
import pl.mateusz.example.friendoo.gender.Gender;
import pl.mateusz.example.friendoo.gender.UserGender;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserRepository;
import pl.mateusz.example.friendoo.user.UserService;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetController;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetDto;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetTokenDto;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetTokenService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserPasswordResetController.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
public class UserPasswordResetControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserPasswordResetTokenService userPasswordResetTokenService;

  @MockBean
  private UserRepository userRepository;

  private User user;

  private String tokenCode = "tokencode123456";
  private UserPasswordResetTokenDto userPasswordResetTokenDto;
  private UserPasswordResetDto userPasswordResetDto;

  @BeforeEach
  public void setUp() {
    user = createTestUser(1L, "Tomasz", "Nowak", "tnowak@byom.de",
      LocalDate.of(1993, 10, 20), Gender.MAN, "password",
      LocalDateTime.now().minusDays(10), true);
      when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(true);
      userPasswordResetTokenDto = new UserPasswordResetTokenDto();
      userPasswordResetTokenDto.setUserEmail(user.getEmail());
      userPasswordResetTokenDto.setTokenCode(tokenCode);
      userPasswordResetDto = new UserPasswordResetDto();
      userPasswordResetDto.setEmail(user.getEmail());
      userPasswordResetDto.setPassword("NewPassword123!");
      userPasswordResetDto.setRepeatedPassword("NewPassword123!");
    }

  @Nested
  class displayPasswordResetEmailFormTests {
    @Test
    public void shouldDisplayPasswordResetEmailForm() throws Exception {
      mockMvc.perform(get("/password-reset-email"))
        .andExpect(status().isOk())
        .andExpect(view().name("password-reset-email-form"));
    }
  }

  @Nested
  class passwordResetEmailTests {

    @Test
    public void shouldSendPasswordResetLinkSuccessfully() throws Exception {
      // given
      doNothing().when(userService).sendPasswordResetLink(user.getEmail());
      // when
      mockMvc.perform(post("/password-reset-email")
          .param("email", user.getEmail()))
        .andExpect(status().isOk())
        .andExpect(view().name("password-reset-email-success"));
      // then
      verify(userService, times(1)).sendPasswordResetLink(user.getEmail());
    }

    @Test
    public void shouldNotSendPasswordResetEmailAndReturnFormWhenUserDoesNotExist() throws Exception {
      // given
      String notExistingEmail = "notexistingemail@byom.de";
      // when
      mockMvc.perform(post("/password-reset-email")
          .param("email", notExistingEmail))
        .andExpect(status().isOk())
        .andExpect(view().name("password-reset-email-form"));

      // then
      verify(userService, times(0)).sendPasswordResetLink(notExistingEmail);
    }

    @Test
    public void shouldReturnPasswordResetEmailFormAndShowErrorWhenEmailSendingFails() throws Exception {
      // given
      doThrow(new MailSendingException("Błąd wysyłania emaila"))
        .when(userService).sendPasswordResetLink(user.getEmail());
      // when
      mockMvc.perform(post("/password-reset-email")
          .param("email", user.getEmail()))
        .andExpect(status().isOk())
        .andExpect(view().name("password-reset-email-form"))
        .andExpect(model().attributeExists("errorMessage"))
          .andExpect(model().attribute("errorMessage",
            "Wystąpił problem z wysyłką wiadomości e-mail. Spróbuj ponownie później."));
      // then
      verify(userService, times(1)).sendPasswordResetLink(user.getEmail());
    }

    @Test
    public void shouldReturnPasswordResetEmailFormWhenEmailIsEmpty() throws Exception {
      // given
      String emptyEmail = "";
      // when
      mockMvc.perform(post("/password-reset-email")
          .param("email", emptyEmail))
        .andExpect(status().isOk())
        .andExpect(view().name("password-reset-email-form"));
      // then
      verify(userService, times(0)).sendPasswordResetLink(user.getEmail());
    }

  }

  @Nested
  class displayPasswordResetFormTests {
    @Test
    public void shouldDisplayPasswordResetFormSuccessfully() throws Exception {
      // given
      when(userPasswordResetTokenService.getValidUserPasswordResetTokenByTokenCode(tokenCode))
        .thenReturn(Optional.of(userPasswordResetTokenDto));
      when(userService.getUserPasswordResetDtoByEmail(user.getEmail()))
        .thenReturn(Optional.of(userPasswordResetDto));
      // when
      mockMvc.perform(get("/password-reset")
          .param("key", userPasswordResetTokenDto.getTokenCode()))
        .andExpect(status().isOk())
        .andExpect(view().name("password-reset-form"))
        .andExpect(model().attributeExists("userPasswordResetDto"))
        .andExpect(model().attribute("userPasswordResetDto", userPasswordResetDto));
      // then
      verify(userPasswordResetTokenService, times(1))
        .getValidUserPasswordResetTokenByTokenCode(tokenCode);
      verify(userService, times(1))
        .getUserPasswordResetDtoByEmail(user.getEmail());
    }

    @Test
    public void shouldReturnError404TemplateWhenTokenCodeIsIncorrect() throws Exception {
      // given
      String wrongToken = "wrongtoken";
      when(userPasswordResetTokenService.getValidUserPasswordResetTokenByTokenCode(wrongToken))
        .thenReturn(Optional.empty());
      // when
      mockMvc.perform(get("/password-reset")
          .param("key", wrongToken))
        .andExpect(status().isOk())
        .andExpect(view().name("error/404"));
      // then
      verify(userService, times(0))
        .getUserPasswordResetDtoByEmail(user.getEmail());
    }

  }

  @Nested
  class resetPasswordTests {

    @Test
    public void shouldResetPasswordSuccessfullyAndReturnPasswordResetSuccessTemplate() throws Exception {
      // given
      when(userRepository.existsUserByEmail(userPasswordResetDto.getEmail())).thenReturn(true);
      when(userService.resetPassword(userPasswordResetDto)).thenReturn(true);
      // when && then
      mockMvc.perform(post("/password-reset")
          .param("key", tokenCode)
          .param("email", userPasswordResetDto.getEmail())
          .param("password", userPasswordResetDto.getPassword())
          .param("repeatedPassword", userPasswordResetDto.getRepeatedPassword()))
        .andExpect(status().isOk())
        .andExpect(view().name("password-reset-success"));
    }

    @Test
    public void shouldReturnError505TemplateWhenPasswordResetFailed() throws Exception {
      // given
      when(userRepository.existsUserByEmail(userPasswordResetDto.getEmail())).thenReturn(true);
      when(userService.resetPassword(userPasswordResetDto)).thenReturn(false);
      // when && then
      mockMvc.perform(post("/password-reset")
          .param("key", tokenCode)
          .param("email", userPasswordResetDto.getEmail())
          .param("password", userPasswordResetDto.getPassword())
          .param("repeatedPassword", userPasswordResetDto.getRepeatedPassword()))
        .andExpect(status().isOk())
        .andExpect(view().name("error/500"));
    }

    @Test
    public void shouldReturnPasswordResetFormWhenPasswordsDoNotMatchPattern() throws Exception {
      // given
      String incorrectPassword = "abc";
      userPasswordResetDto.setPassword(incorrectPassword);
      userPasswordResetDto.setRepeatedPassword(incorrectPassword);
      when(userRepository.existsUserByEmail(userPasswordResetDto.getEmail())).thenReturn(true);
      when(userService.resetPassword(userPasswordResetDto)).thenReturn(false);
      // when
      mockMvc.perform(post("/password-reset")
          .param("key", tokenCode)
          .param("email", userPasswordResetDto.getEmail())
          .param("password", userPasswordResetDto.getPassword())
          .param("repeatedPassword", userPasswordResetDto.getRepeatedPassword()))
        .andExpect(status().isOk())
        .andExpect(view().name("password-reset-form"));
      // then
      verify(userService, times(0)).resetPassword(userPasswordResetDto);
    }

    @Test
    public void shouldReturnPasswordResetFormWhenUserWithProvidedEmailDoesNotExist() throws Exception {
      // given
      when(userRepository.existsUserByEmail(userPasswordResetDto.getEmail())).thenReturn(false);
      // when
      mockMvc.perform(post("/password-reset")
          .param("key", tokenCode)
          .param("email", userPasswordResetDto.getEmail())
          .param("password", userPasswordResetDto.getPassword())
          .param("repeatedPassword", userPasswordResetDto.getRepeatedPassword()))
        .andExpect(status().isOk())
        .andExpect(view().name("password-reset-form"));
      // then
      verify(userService, times(0)).resetPassword(userPasswordResetDto);
    }

  }

  private User createTestUser(Long id, String firstName, String lastName, String email, LocalDate dateOfBirth,
                              Gender  gender, String password, LocalDateTime joinedAt, boolean isActiveAccount) {
    User user = new User();
    user.setId(id);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setDateOfBirth(dateOfBirth);
    UserGender userGender = new UserGender();
    userGender.setGender(gender);
    user.setPassword(password);
    user.setJoinedAt(joinedAt);
    user.setActiveAccount(isActiveAccount);
    return user;
  }

}
