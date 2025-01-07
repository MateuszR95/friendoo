package pl.mateusz.example.friendoo.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetTokenService;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class MailService {
  private final JavaMailSender javaMailSender;
  @Value("${spring.mail.username}")
  private String ownerEmail;

  private final UserPasswordResetTokenService passwordResetTokenService;
  private static final String ACTIVATION_MAIL_TITLE = "Friendoo- aktywuj swoje konto";

  private static final String PASSWORD_RESET_MAIL_TITLE = "Friendoo- zresetuj swoje hasło";
  Logger logger = LoggerFactory.getLogger(MailService.class);

  public MailService(JavaMailSender javaMailSender, UserPasswordResetTokenService
      passwordResetTokenService) {
    this.javaMailSender = javaMailSender;
    this.passwordResetTokenService = passwordResetTokenService;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public boolean sendActivationCode(String receiverMail, String receiverName, String token) {
    logger.info("Wysyłam maila do {}", receiverMail);
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setFrom(ownerEmail);
      helper.setTo(receiverMail);
      helper.setSubject(ACTIVATION_MAIL_TITLE);
      String content = "<html><body>";
      content += "<p>Witaj " + receiverName + "! Oto twój kod weryfikacyjny: <strong>"
        + token + "</strong></p>";
      content += "<p>Token jest ważny przez 15 minut.</p>";
      content += "</body></html>";
      helper.setText(content, true);
      javaMailSender.send(mimeMessage);
      logger.info("Mail do {} wysłany pomyślnie", receiverMail);
      return true;
    } catch (MessagingException e) {
      logger.error("Błąd podczas wysyłania wiadomości");
      return false;
    }
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public boolean sendPasswordResetEmail(String userEmail) {
    logger.info("Wysyłam maila do {}", userEmail);
    try {
      String content = createPasswordResetEmailContent(userEmail);
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setFrom(ownerEmail);
      helper.setTo(userEmail);
      helper.setSubject(PASSWORD_RESET_MAIL_TITLE);
      helper.setText(content, true);
      javaMailSender.send(mimeMessage);
      logger.info("Mail do {} wysłany pomyślnie", userEmail);
      return true;
    } catch (MessagingException e) {
      logger.error("Błąd podczas wysyłania wiadomości do {}: {}", userEmail, e.getMessage());
      return false;

    }
  }

  private String createPasswordResetEmailContent(String email) {
    String message = "Skorzystaj z linku poniżej w celu zresetowania hasła. "
        + "Link jest ważny przez 24 godziny";
    String buttonText = "Resetuj hasło";
    String buttonUrl = buildPasswordResetUrl(email);
    String content = "<html><body>";
    content += "<p>" + message + "</p>";
    content += "<a href=\"" + buttonUrl + "\" style=\"background-color:"
      + " #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: "
      + "none; display: inline-block; font-size: 16px; border-radius: 5px;\">" + buttonText
      + "</a>";
    content += "</body></html>";
    return content;
  }

  private String buildPasswordResetUrl(String email) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
      .path("/password-reset")
      .queryParam("key", passwordResetTokenService.getUserPasswordResetTokenByEmail(email)
        .getToken())
        .toUriString();
  }


}
