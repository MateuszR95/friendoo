package pl.mateusz.example.friendoo.validator.pattern;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class ValidationPatterns {

  public static final String PASSWORD_REGEX_PATTERN = "^(?=.*[A-Z])"
      + "(?=.*[a-z])(?=.*[#?!@$%^&*-]).+$";

  public static final String EMAIL_REGEX_PATTERN =
      "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)"
      + "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

  private ValidationPatterns() {
  }
}
