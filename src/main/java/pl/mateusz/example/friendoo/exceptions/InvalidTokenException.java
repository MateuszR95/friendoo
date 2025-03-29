package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when an invalid token is provided.
 */
public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(String message) {
    super(message);
  }
}
