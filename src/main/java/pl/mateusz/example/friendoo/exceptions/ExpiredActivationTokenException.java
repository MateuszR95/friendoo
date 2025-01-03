package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class ExpiredActivationTokenException extends RuntimeException {

  public ExpiredActivationTokenException(String message) {
    super(message);
  }
}
