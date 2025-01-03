package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserPasswordResetTokenException extends RuntimeException {

  public UserPasswordResetTokenException(String message) {
    super(message);
  }
}
