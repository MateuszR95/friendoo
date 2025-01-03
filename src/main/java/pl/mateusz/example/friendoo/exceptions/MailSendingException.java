package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class MailSendingException extends RuntimeException {

  public MailSendingException(String message) {
    super(message);
  }
}
