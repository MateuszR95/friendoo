package pl.mateusz.example.friendoo.user.location;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.mateusz.example.friendoo.exceptions.UserLocationApiException;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class UserLocationService {

  private final RestTemplate restTemplate;
  private static final String USER_LOCATION_API_URL = "https://nominatim.openstreetmap.org/search?";

  public UserLocationService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public List<String> getLocations(String query) {
    String queryWithoutDiacritics  = StringUtils.stripAccents(query);
    String url = UriComponentsBuilder.fromHttpUrl(USER_LOCATION_API_URL)
          .queryParam("q", queryWithoutDiacritics)
          .queryParam("format", "json")
          .queryParam("addressdetails", 1)
          .queryParam("limit", 100)
          .build()
          .toUriString();
    try {
      UserLocation[] locations = restTemplate.getForObject(url, UserLocation[].class);
      if (locations == null) {
        return Collections.emptyList();
      }
      return Arrays.stream(locations)
        .map(UserLocation::getDisplayName)
        .collect(Collectors.toList());
    } catch (RestClientException e) {
      throw new UserLocationApiException("Błąd połączenia", e);
    }
  }




}
