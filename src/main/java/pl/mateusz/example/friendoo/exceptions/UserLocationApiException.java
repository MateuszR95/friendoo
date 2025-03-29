package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when an error occurs while fetching user location.
 */
public class UserLocationApiException extends RuntimeException {

  public UserLocationApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
