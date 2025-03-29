package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when a user location is not found.
 */
public class UserLocationNotFoundException extends RuntimeException {

  public UserLocationNotFoundException(String message) {
    super(message);
  }
}
