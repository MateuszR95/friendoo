package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(String message) {
    super(message);
  }
}
