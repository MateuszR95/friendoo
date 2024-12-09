package pl.mateusz.example.friendoo.user.location;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
@ToString
public class UserLocationDto {

  private String name;
  private String postalCode;
  private String countryCode;
  private String state;
  private String municipality;

}
