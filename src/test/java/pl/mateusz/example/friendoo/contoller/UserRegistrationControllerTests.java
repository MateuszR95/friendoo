package pl.mateusz.example.friendoo.contoller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSendException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.mateusz.example.friendoo.exceptions.AccountAlreadyActivatedException;
import pl.mateusz.example.friendoo.exceptions.ExpiredActivationTokenException;
import pl.mateusz.example.friendoo.exceptions.InvalidTokenException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.gender.Gender;
import pl.mateusz.example.friendoo.user.UserDisplayDto;
import pl.mateusz.example.friendoo.user.UserRepository;
import pl.mateusz.example.friendoo.user.UserService;
import pl.mateusz.example.friendoo.user.registration.UserRegistrationController;
import pl.mateusz.example.friendoo.user.registration.UserRegistrationDto;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRegistrationController.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
public class UserRegistrationControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;
  @Nested
  class displayRegistrationFormTests {
    @Test
    public void shouldReturnRegistrationForm() throws Exception {
      mockMvc.perform(get("/registration"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("userRegistrationDto"))
        .andExpect(view().name("registration-form"));
    }
  }

  @Nested
  class registerTests {
    private UserRegistrationDto userRegistrationDto;
    @BeforeEach
    public void setup() {
      userRegistrationDto = new UserRegistrationDto();
      userRegistrationDto.setEmail("tnowak123@byom.de");
      userRegistrationDto.setPassword("Password1234!");
      userRegistrationDto.setRepeatedPassword("Password1234!");
      userRegistrationDto.setDateOfBirth(LocalDate.of(1995, 6, 12));
      userRegistrationDto.setFirstName("Tomasz");
      userRegistrationDto.setLastName("Nowak");
      userRegistrationDto.setGender(Gender.MAN.name());
    }
    @Test
    public void shouldRegisterUserSuccessfullyAndRedirectToActivationPage() throws Exception {
      // given
      when(userRepository.existsUserByEmail(userRegistrationDto.getEmail()))
        .thenReturn(false);
      // when
      mockMvc.perform(post("/registration")
        .param("email", userRegistrationDto.getEmail())
        .param("password", userRegistrationDto.getPassword())
        .param("repeatedPassword", userRegistrationDto.getRepeatedPassword())
        .param("dateOfBirth", userRegistrationDto.getDateOfBirth().toString())
        .param("firstName", userRegistrationDto.getFirstName())
        .param("lastName", userRegistrationDto.getLastName())
        .param("gender", userRegistrationDto.getGender()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/activation"));
      // then
      verify(userService, times(1))
        .registerAccount(userRegistrationDto);
    }

    @Test
    public void shouldNotRegisterAndReturnRegistrationFormWhenUserWithProvidedEmailAlreadyExists()
      throws Exception {
      // given
      when(userRepository.existsUserByEmail(userRegistrationDto.getEmail()))
        .thenReturn(true);
      // when
      mockMvc.perform(post("/registration")
          .param("email", userRegistrationDto.getEmail())
          .param("password", userRegistrationDto.getPassword())
          .param("repeatedPassword", userRegistrationDto.getRepeatedPassword())
          .param("dateOfBirth", userRegistrationDto.getDateOfBirth().toString())
          .param("firstName", userRegistrationDto.getFirstName())
          .param("lastName", userRegistrationDto.getLastName())
          .param("gender", userRegistrationDto.getGender()))
        .andExpect(status().isOk())
        .andExpect(view().name("registration-form"));
      // then
      verify(userService, times(0))
        .registerAccount(userRegistrationDto);
    }

    @Test
    public void shouldNotRegisterAndReturnRegistrationFormWhenPasswordFailedPatternValidation()
      throws Exception {
      // given
      userRegistrationDto.setPassword("abc");
      userRegistrationDto.setRepeatedPassword("abc");
      when(userRepository.existsUserByEmail(userRegistrationDto.getEmail()))
        .thenReturn(false);
      // when
      mockMvc.perform(post("/registration")
          .param("email", userRegistrationDto.getEmail())
          .param("password", userRegistrationDto.getPassword())
          .param("repeatedPassword", userRegistrationDto.getRepeatedPassword())
          .param("dateOfBirth", userRegistrationDto.getDateOfBirth().toString())
          .param("firstName", userRegistrationDto.getFirstName())
          .param("lastName", userRegistrationDto.getLastName())
          .param("gender", userRegistrationDto.getGender()))
        .andExpect(status().isOk())
        .andExpect(view().name("registration-form"));
      // then
      verify(userService, times(0))
        .registerAccount(userRegistrationDto);
    }

    @Test
    public void shouldNotRegisterAndReturnRegistrationFormWhenUserAgeIsLowerThanRequired()
      throws Exception {
      // given
      userRegistrationDto.setDateOfBirth(LocalDate.of(2026, 2, 12));
      when(userRepository.existsUserByEmail(userRegistrationDto.getEmail()))
        .thenReturn(false);
      // when
      mockMvc.perform(post("/registration")
          .param("email", userRegistrationDto.getEmail())
          .param("password", userRegistrationDto.getPassword())
          .param("repeatedPassword", userRegistrationDto.getRepeatedPassword())
          .param("dateOfBirth", userRegistrationDto.getDateOfBirth().toString())
          .param("firstName", userRegistrationDto.getFirstName())
          .param("lastName", userRegistrationDto.getLastName())
          .param("gender", userRegistrationDto.getGender()))
        .andExpect(status().isOk())
        .andExpect(view().name("registration-form"));
      // then
      verify(userService, times(0))
        .registerAccount(userRegistrationDto);
    }

    @Test
    public void shouldNotRegisterAndReturnRegistrationFormWhenPasswordsDoNotMatch()
      throws Exception {
      // given
      userRegistrationDto.setPassword("Password1234!");
      userRegistrationDto.setRepeatedPassword("Password4567!");
      when(userRepository.existsUserByEmail(userRegistrationDto.getEmail()))
        .thenReturn(false);
      // when
      mockMvc.perform(post("/registration")
          .param("email", userRegistrationDto.getEmail())
          .param("password", userRegistrationDto.getPassword())
          .param("repeatedPassword", userRegistrationDto.getRepeatedPassword())
          .param("dateOfBirth", userRegistrationDto.getDateOfBirth().toString())
          .param("firstName", userRegistrationDto.getFirstName())
          .param("lastName", userRegistrationDto.getLastName())
          .param("gender", userRegistrationDto.getGender()))
        .andExpect(status().isOk())
        .andExpect(view().name("registration-form"));
      // then
      verify(userService, times(0))
        .registerAccount(userRegistrationDto);
    }

    @Test
    public void shouldNotRegisterAndReturnRegistrationFormWhenMailSendExceptionOccurs()
      throws Exception {
      when(userRepository.existsUserByEmail(userRegistrationDto.getEmail()))
        .thenReturn(false);
      doThrow(new MailSendException("")).when(userService).registerAccount(userRegistrationDto);
      // when
      mockMvc.perform(post("/registration")
          .param("email", userRegistrationDto.getEmail())
          .param("password", userRegistrationDto.getPassword())
          .param("repeatedPassword", userRegistrationDto.getRepeatedPassword())
          .param("dateOfBirth", userRegistrationDto.getDateOfBirth().toString())
          .param("firstName", userRegistrationDto.getFirstName())
          .param("lastName", userRegistrationDto.getLastName())
          .param("gender", userRegistrationDto.getGender()))
        .andExpect(status().isOk())
        .andExpect(view().name("registration-form"));
      // then
      verify(userService, times(1))
        .registerAccount(userRegistrationDto);
    }

    @Nested
    class displayAccountActivationFormTests {

      @Test
      public void shouldDisplayAccountActivationFormSuccessfullyWhenUserIsNotActivated() throws Exception {
        // given
        String testEmail = "tnowak123@byom.de";
        // when && then
        when(userService.isAccountActivated(testEmail)).thenReturn(false);
        mockMvc.perform(get("/activation")
            .sessionAttr("pendingActivationEmail", testEmail))
          .andExpect(status().isOk())
          .andExpect(model().attributeExists("userEmail"))
          .andExpect(model().attribute("userEmail", testEmail))
          .andExpect(view().name("registration-activation"));
        verify(userService, times(1)).isAccountActivated(testEmail);
      }

      @Test
      public void shouldRedirectToHomePageWhenUserIsAlreadyActivated() throws Exception {
        // given
        String testEmail = "tnowak123@byom.de";
        // when && then
        when(userService.isAccountActivated(testEmail)).thenReturn(true);
        mockMvc.perform(get("/activation")
            .sessionAttr("pendingActivationEmail", testEmail))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/"));
        verify(userService, times(1)).isAccountActivated(testEmail);
      }

      @Test
      public void shouldRedirectToRegistrationPageWhenUserEmailIsEmpty() throws Exception {
        // given
        String testEmail = "";
        // when && then
        mockMvc.perform(get("/activation")
            .sessionAttr("pendingActivationEmail", testEmail))
          .andExpect(status().is3xxRedirection())
          .andExpect(flash().attribute("errorMessage", "Sesja wygasła. Zarejestruj się ponownie."))
          .andExpect(redirectedUrl("/registration"));
        verify(userService, times(0)).isAccountActivated(testEmail);
      }

      @Test
      public void shouldRedirectToRegistrationPageWhenUserEmailIsBlank() throws Exception {
        // given
        String testEmail = "     ";
        // when && then
        mockMvc.perform(get("/activation")
            .sessionAttr("pendingActivationEmail", testEmail))
          .andExpect(status().is3xxRedirection())
          .andExpect(flash().attribute("errorMessage", "Sesja wygasła. Zarejestruj się ponownie."))
          .andExpect(redirectedUrl("/registration"));
        verify(userService, times(0)).isAccountActivated(testEmail);
      }

      @Test
      public void shouldRedirectToRegistrationPageWhenUserEmailIsNull() throws Exception {
        // given
        // when && then
        mockMvc.perform(get("/activation"))
          .andExpect(status().is3xxRedirection())
          .andExpect(flash().attribute("errorMessage", "Sesja wygasła. Zarejestruj się ponownie."))
          .andExpect(redirectedUrl("/registration"));
        verify(userService, times(0)).isAccountActivated(anyString());
      }

    }

    @Nested
    class activateAccountTests {
      private UserDisplayDto userDisplayDto;
      @BeforeEach
      public void setUp() {
        userDisplayDto = new UserDisplayDto();
        userDisplayDto.setFirstName("Adam");
        userDisplayDto.setLastName("Nowak");
        userDisplayDto.setId(12L);
      }
      @Test
      public void shouldActivateAccountSuccessfullyAndReturnActivationSuccessTemplate() throws Exception {
        // given
        String token = "token1234";
        String email = "anowak@byom.de";
        doNothing().when(userService).activateAccount(token, email);
        when(userService.findUserToDisplay(anyString())).thenReturn(Optional.of(userDisplayDto));
        // when && then
        mockMvc.perform(post("/activation")
            .param("enteredToken", token)
            .param("userEmail", email))
          .andExpect(status().isOk())
          .andExpect(model().attribute("userName", userDisplayDto.getFirstName()))
          .andExpect(view().name("activation-success"));
        verify(userService, times(1)).activateAccount(email, token);
      }

      @Test
      public void shouldNotActivateAccountAndReturnRegistrationActivationTemplateWhenInvalidTokenExceptionOccurs()
        throws Exception {
        // given
        String token = "token1234";
        String email = "anowak@byom.de";
        doThrow(new InvalidTokenException("Wprowadzony kod aktywacyjny jest niepoprawny"))
          .when(userService).activateAccount(email, token);
        // when && then
        mockMvc.perform(post("/activation")
            .param("enteredToken", token)
            .param("userEmail", email))
          .andExpect(status().isOk())
          .andExpect(model().attribute("userEmail", email))
          .andExpect(model().attribute("errorMessage", "Wprowadzony kod aktywacyjny jest niepoprawny"))
          .andExpect(view().name("registration-activation"));
        verify(userService, times(1)).activateAccount(email, token);
      }

      @Test
      public void shouldNotActivateAccountAndReturnRegistrationActivationTemplateWhenUserNotFoundExceptionOccurs()
        throws Exception {
        // given
        String token = "token1234";
        String email = "anowak@byom.de";
        doThrow(new UserNotFoundException("Nie znaleziono użytkownika"))
          .when(userService).activateAccount(email, token);
        // when && then
        mockMvc.perform(post("/activation")
            .param("enteredToken", token)
            .param("userEmail", email))
          .andExpect(status().isOk())
          .andExpect(model().attribute("userEmail", email))
          .andExpect(model().attribute("errorMessage", "Nie znaleziono użytkownika"))
          .andExpect(view().name("registration-activation"));
        verify(userService, times(1)).activateAccount(email, token);
      }

      @Test
      public void shouldNotActivateAccountAndReturnRegistrationActivationTemplateWhenAccountAlreadyActivatedExceptionOccurs()
        throws Exception {
        // given
        String token = "token1234";
        String email = "anowak@byom.de";
        doThrow(new AccountAlreadyActivatedException("Konto już zostało aktywowane."))
          .when(userService).activateAccount(email, token);
        // when && then
        mockMvc.perform(post("/activation")
            .param("enteredToken", token)
            .param("userEmail", email))
          .andExpect(status().isOk())
          .andExpect(model().attribute("userEmail", email))
          .andExpect(model().attribute("errorMessage", "Konto już zostało aktywowane."))
          .andExpect(view().name("registration-activation"));
        verify(userService, times(1)).activateAccount(email, token);
      }

      @Test
      public void shouldNotActivateAccountAndReturnRegistrationActivationTemplateWhenExpiredActivationTokenExceptionOccurs()
        throws Exception {
        // given
        String token = "token1234";
        String email = "anowak@byom.de";
        doThrow(new ExpiredActivationTokenException("Twój kod aktywacyjny wygasł"))
          .when(userService).activateAccount(email, token);
        // when && then
        mockMvc.perform(post("/activation")
            .param("enteredToken", token)
            .param("userEmail", email))
          .andExpect(status().isOk())
          .andExpect(view().name("activation-failed"));
        verify(userService, times(1)).activateAccount(email, token);
      }
    }

    @Nested
    class displayActivationFailPageTests {
      @Test
      public void shouldDisplayActivationFailPageSuccessfully() throws Exception {
        mockMvc.perform(get("/activation-failed"))
          .andExpect(status().isOk())
          .andExpect(view().name("activation-failed"));
      }
    }

  }
}
