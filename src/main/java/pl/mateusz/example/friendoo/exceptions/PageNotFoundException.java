package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when a page is not found.
 * This exception is used to indicate that a requested page does not exist in the system.
 */
public class PageNotFoundException extends RuntimeException {

  public PageNotFoundException(String message) {
    super(message);
  }
}
