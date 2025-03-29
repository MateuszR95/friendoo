package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when an activation token has expired.
 */
public class ExpiredActivationTokenException extends RuntimeException {

  public ExpiredActivationTokenException(String message) {
    super(message);
  }
}
