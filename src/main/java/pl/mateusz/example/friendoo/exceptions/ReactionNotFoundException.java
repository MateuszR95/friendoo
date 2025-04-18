package pl.mateusz.example.friendoo.exceptions;

/**
 * Custom exception class to handle cases where a reaction is not found.
 * This exception can be thrown when a requested reaction does not exist
 * in the database or the application context.
 */
public class ReactionNotFoundException extends RuntimeException {

  public ReactionNotFoundException(String message) {
    super(message);
  }
}
