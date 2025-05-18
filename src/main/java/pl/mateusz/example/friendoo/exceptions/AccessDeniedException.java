package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when access is denied to a resource or action.
 */
public class AccessDeniedException extends RuntimeException {

  public AccessDeniedException(String message) {
    super(message);
  }
}
