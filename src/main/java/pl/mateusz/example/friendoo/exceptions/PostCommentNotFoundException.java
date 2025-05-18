package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when a post comment is not found.
 * This exception is used to indicate that a specific post comment
 * could not be located in the system.
 */
public class PostCommentNotFoundException extends RuntimeException {

  public PostCommentNotFoundException(String message) {
    super(message);
  }
}
