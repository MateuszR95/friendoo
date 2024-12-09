package pl.mateusz.example.friendoo.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class MailService {
  private final JavaMailSender javaMailSender;
  @Value("${spring.mail.username}")
  private String ownerEmail;
  private static final String ACTIVATION_MAIL_TITLE = "Friendoo- aktywuj swoje konto";
  Logger logger = LoggerFactory.getLogger(MailService.class);

  public MailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
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
      helper.setText(content, true);;
      javaMailSender.send(mimeMessage);
      logger.info("Mail do {} wysłany pomyślnie", receiverMail);
      return true;
    } catch (MessagingException e) {
      logger.error("Błąd podczas wysyłania wiadomości");
      return false;
    }

  }

}
