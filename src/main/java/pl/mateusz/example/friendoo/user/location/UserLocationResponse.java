package pl.mateusz.example.friendoo.user.location;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
public class UserLocationResponse {

  private List<UserLocation> postalCodes = new ArrayList<>();
}
