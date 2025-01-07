package pl.mateusz.example.friendoo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.mateusz.example.friendoo.email.MailService;
import pl.mateusz.example.friendoo.gender.Gender;
import pl.mateusz.example.friendoo.gender.UserGender;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetToken;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetTokenService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailServiceTests {

  @Mock
  private JavaMailSender javaMailSender;
  @Mock
  private UserPasswordResetTokenService passwordResetTokenService;
  @Mock
  private MimeMessage mimeMessage;
  @InjectMocks
  private MailService mailService;


  @Nested
  class sendActivationCodeTests {

    private final String receiverMail = "receiver@byom.de";
    private final String token = "abcdef";
    private final String receiverName = "Tomasz";

    @BeforeEach
      void setup() {
      ReflectionTestUtils.setField(mailService, "ownerEmail", "owner@byom.de");
    }
    @Test
    public void shouldSendActivationCodeWhenValidInputProvided (){
      // given
      // when
      when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
      boolean result = mailService.sendActivationCode(receiverMail, receiverName, token);
      // then
      assertTrue(result);
      verify(javaMailSender, times(1)).createMimeMessage();
      verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void shouldFailToSendActivationCodeWhenMessagingExceptionThrown () {
      // given
      // when
      when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
      doAnswer(invocation -> {
        throw new MessagingException();
      }).when(javaMailSender).send(mimeMessage);
      boolean result = mailService.sendActivationCode(receiverMail, receiverName, token);
      // then
      assertFalse(result);
      verify(javaMailSender, times(1)).createMimeMessage();
      verify(javaMailSender, times(1)).send(mimeMessage);
    }

  }

  @Nested
  class sendPasswordResetEmailTests {

    private UserPasswordResetToken userPasswordResetToken;
    private User user;
    @BeforeEach
    void setup() {
      ReflectionTestUtils.setField(mailService, "ownerEmail", "owner@byom.de");
      MockHttpServletRequest request = new MockHttpServletRequest();
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
      user = createTestUser(1L, "Tomasz", "Nowak", "tnowak@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "password", LocalDateTime.now().minusDays(10),
        true);
      userPasswordResetToken = createUserPasswordResetToken(10L, user, "abcdef", LocalDateTime.now().minusMinutes(10),
        LocalDateTime.now().plusMinutes(10), null, false, true);
    }

    @Test
    public void shouldSendPasswordResetEmailWhenValidInputProvided() {
      // give
      // when
      when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
      when(passwordResetTokenService.getUserPasswordResetTokenByEmail(user.getEmail())).thenReturn(userPasswordResetToken);
      boolean result = mailService.sendPasswordResetEmail(user.getEmail());
      // then
      assertTrue(result);
      verify(javaMailSender, times(1)).createMimeMessage();
      verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void shouldFailToSendPasswordResetEmailWhenMessagingExceptionThrown () {
      // given
      // when
      when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
      when(passwordResetTokenService.getUserPasswordResetTokenByEmail(user.getEmail())).thenReturn(userPasswordResetToken);
      doAnswer(invocation -> {
        throw new MessagingException();
      }).when(javaMailSender).send(mimeMessage);
      boolean result = mailService.sendPasswordResetEmail(user.getEmail());
      // then
      assertFalse(result);
      verify(javaMailSender, times(1)).createMimeMessage();
      verify(javaMailSender, times(1)).send(mimeMessage);
    }
  }

  private UserPasswordResetToken createUserPasswordResetToken(Long id, User user, String tokenCode,
                                                              LocalDateTime creationDate, LocalDateTime expireDate,
                                                              LocalDateTime usedDate, boolean isUsed, boolean isValid) {
    UserPasswordResetToken userPasswordResetToken = new UserPasswordResetToken();
    userPasswordResetToken.setId(id);
    userPasswordResetToken.setUser(user);
    userPasswordResetToken.setToken(tokenCode);
    userPasswordResetToken.setCreationDate(creationDate);
    userPasswordResetToken.setExpireDate(expireDate);
    userPasswordResetToken.setUsedDate(usedDate);
    userPasswordResetToken.setUsed(isUsed);
    userPasswordResetToken.setValid(isValid);
    return userPasswordResetToken;
  }

  private User createTestUser(Long id, String firstName, String lastName, String email, LocalDate dateOfBirth,
                              Gender gender, String password, LocalDateTime joinedAt, boolean isActiveAccount) {
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
