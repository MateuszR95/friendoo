package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when a page category is not found.
 */
public class PageCategoryNotFoundException extends RuntimeException {

  public PageCategoryNotFoundException(String message) {
    super(message);
  }
}
