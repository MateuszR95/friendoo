package pl.mateusz.example.friendoo.validation.pattern;

/**
 * Utility class containing regex patterns for validation.
 */
public class ValidationPatterns {

  public static final String PASSWORD_REGEX_PATTERN = "^(?=.*[A-Z])"
      + "(?=.*[a-z])(?=.*[#?!@$%^&*-]).+$";

  public static final String EMAIL_REGEX_PATTERN =
      "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)"
      + "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

  public static final String USER_PHONE_NUMBER_PATTERN = "^\\+[1-9]\\d{1,14}$";

  private ValidationPatterns() {
  }
}
