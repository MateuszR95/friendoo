package pl.mateusz.example.friendoo.user.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
public class UserLocation {

  private String adminCode2;
  private String adminCode3;
  private String adminName3;
  private String adminCode1;
  private String adminName2;
  private double lng;
  private String countryCode;
  private String postalCode;
  private String adminName1;
  @JsonProperty("ISO3166-2")
  private String iso31662;
  private String placeName;
  private double lat;

}

