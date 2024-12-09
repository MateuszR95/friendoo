package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class AccountNotActivatedException extends RuntimeException {

  public AccountNotActivatedException(String message) {
    super(message);
  }
}
