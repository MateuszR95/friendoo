package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when a user password reset token is invalid.
 */
public class UserPasswordResetTokenException extends RuntimeException {

  public UserPasswordResetTokenException(String message) {
    super(message);
  }
}
