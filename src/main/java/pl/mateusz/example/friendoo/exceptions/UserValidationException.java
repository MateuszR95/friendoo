package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when a user validation fails.
 */
public class UserValidationException extends RuntimeException {

  public UserValidationException(String message) {
    super(message);
  }
}
