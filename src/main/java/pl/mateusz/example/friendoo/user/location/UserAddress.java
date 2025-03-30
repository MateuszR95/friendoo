package pl.mateusz.example.friendoo.user.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an address.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_addresses")
public class UserAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonProperty("isolated_dwelling")
  @Column(name = "isolated_dwelling")
  private String isolatedDwelling;

  @JsonProperty("city")
  private String city;

  @JsonProperty("town")
  private String town;

  @JsonProperty("administrative")
  private String administrative;

  @JsonProperty("village")
  private String village;

  @JsonProperty("municipality")
  private String municipality;

  @JsonProperty("state_district")
  @Column(name = "state_district")
  private String stateDistrict;

  @JsonProperty("county")
  private String county;

  @JsonProperty("state")
  private String state;

  @JsonProperty("ISO3166-2-lvl4")
  @Column(name = "iso")
  private String iso;

  @JsonProperty("country")
  private String country;

  @JsonProperty("country_code")
  private String countryCode;

}
