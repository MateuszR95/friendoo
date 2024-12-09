package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserLocationApiException extends RuntimeException {

  public UserLocationApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
