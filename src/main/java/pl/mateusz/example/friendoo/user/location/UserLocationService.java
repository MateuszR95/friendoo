package pl.mateusz.example.friendoo.user.location;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.mateusz.example.friendoo.exceptions.UserLocationApiException;

/**
 * Service for managing user location.
 */
@Service
public class UserLocationService {

  private final RestTemplate restTemplate;
  private static final String USER_LOCATION_API_URL = "https://nominatim.openstreetmap.org/search?";

  public UserLocationService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Returns a list of locations based on the query.
   *
   * @param query the query
   * @return the set of locations
   */
  public Set<String> getLocations(String query) {
    String queryWithoutDiacritics  = StringUtils.stripAccents(query);
    String url = UriComponentsBuilder.fromHttpUrl(USER_LOCATION_API_URL)
          .queryParam("q", queryWithoutDiacritics)
          .queryParam("format", "json")
          .queryParam("addressdetails", 1)
          .queryParam("limit", 100)
          .build()
          .toUriString();
    try {
      Location[] locations = restTemplate.getForObject(url, Location[].class);
      if (locations == null) {
        return Collections.emptySet();
      }
      return Arrays.stream(locations)
        .map(Location::getDisplayName)
        .collect(Collectors.toSet());
    } catch (RestClientException e) {
      throw new UserLocationApiException("Błąd połączenia", e);
    }
  }

  /**
   * Returns a location based on the query.
   *
   * @param query the query
   * @return the location
   */
  public Optional<UserAddress> chooseLocation(String query) {
    String queryWithoutDiacritics  = StringUtils.stripAccents(query);
    String url = UriComponentsBuilder.fromHttpUrl(USER_LOCATION_API_URL)
          .queryParam("q", queryWithoutDiacritics)
          .queryParam("format", "json")
          .queryParam("addressdetails", 1)
          .queryParam("limit", 1)
          .build()
          .toUriString();
    try {
      Location[] locations = restTemplate.getForObject(url, Location[].class);
      if (locations == null) {
        return Optional.empty();
      }
      return Optional.of(locations[0].getAddress());
    } catch (RestClientException e) {
      throw new UserLocationApiException("Błąd połączenia", e);
    }
  }

}
