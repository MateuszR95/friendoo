package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when an attempt is made to perform an action on an account
 * that is not activated.
 */
public class AccountNotActivatedException extends RuntimeException {

  public AccountNotActivatedException(String message) {
    super(message);
  }
}
