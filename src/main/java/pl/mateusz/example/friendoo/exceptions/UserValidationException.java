package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserValidationException extends RuntimeException {

  public UserValidationException(String message) {
    super(message);
  }
}
