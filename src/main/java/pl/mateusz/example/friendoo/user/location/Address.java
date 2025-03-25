package pl.mateusz.example.friendoo.user.location;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
public class Address {

  @JsonAnySetter
  private Map<String, Object> addressDetails = new HashMap<>();
}
