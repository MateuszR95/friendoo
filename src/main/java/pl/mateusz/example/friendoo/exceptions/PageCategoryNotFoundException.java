package pl.mateusz.example.friendoo.exceptions;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class PageCategoryNotFoundException extends RuntimeException {

  public PageCategoryNotFoundException(String message) {
    super(message);
  }
}
