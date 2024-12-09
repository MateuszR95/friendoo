package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserLocationNotFoundException extends RuntimeException {

  public UserLocationNotFoundException(String message) {
    super(message);
  }
}
