package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class AccountAlreadyActivatedException extends RuntimeException {

  public AccountAlreadyActivatedException(String message) {
    super(message);
  }
}
