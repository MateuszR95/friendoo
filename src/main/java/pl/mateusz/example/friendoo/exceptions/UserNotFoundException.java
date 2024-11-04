package pl.mateusz.example.friendoo.exceptions;
@SuppressWarnings({"checkstyle:EmptyLineSeparator", "checkstyle:MissingJavadocType"})
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
