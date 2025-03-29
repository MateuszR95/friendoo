package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when an error occurs while sending an email.
 */
public class MailSendingException extends RuntimeException {

  public MailSendingException(String message) {
    super(message);
  }
}
