package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when a user post is not found.
 */
public class UserPostNotFoundException extends RuntimeException {

  public UserPostNotFoundException(String message) {
    super(message);
  }
}
