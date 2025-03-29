package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when an attempt is made to activate an already activated account.
 */
public class AccountAlreadyActivatedException extends RuntimeException {
  public AccountAlreadyActivatedException(String message) {
    super(message);
  }
}
