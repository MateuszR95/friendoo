package pl.mateusz.example.friendoo.user.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
public class UserLocation {

  @JsonProperty("place_id")
  private long placeId;
  private String licence;
  @JsonProperty("osm_type")
  private String osmType;
  @JsonProperty("osm_id")
  private long osmId;
  private double lat;
  private double lon;
  @JsonProperty("class")
  private String clazz;
  private String type;
  @JsonProperty("place_rank")
  private int placeRank;
  private double importance;
  @JsonProperty("addresstype")
  private String addressType;
  private String name;
  @JsonProperty("display_name")
  private String displayName;
  private Address address;
  private double[] boundingbox;


}

