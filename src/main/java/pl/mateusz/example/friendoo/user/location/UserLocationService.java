package pl.mateusz.example.friendoo.user.location;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.mateusz.example.friendoo.exceptions.UserLocationApiException;
import pl.mateusz.example.friendoo.exceptions.UserLocationNotFoundException;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class UserLocationService {

  private final RestTemplate restTemplate;
  private final String username;
  private static final String USER_LOCATION_API_URL = "http://api.geonames.org/postalCodeSearchJSON";

  public UserLocationService(RestTemplate restTemplate,
                             @Value("${geonames.username}") String username) {
    this.restTemplate = restTemplate;
    this.username = username;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public List<UserLocationDto> getLocation(String cityName) {
    String url = UriComponentsBuilder.fromHttpUrl(USER_LOCATION_API_URL)
          .queryParam("placename", cityName)
          .queryParam("maxRows", 30)
          .queryParam("username", username)
          .toUriString();
    try {
      UserLocationResponse userLocationResponse =
          restTemplate.getForObject(url, UserLocationResponse.class);
      if (userLocationResponse == null || userLocationResponse.getPostalCodes().isEmpty()) {
        throw new UserLocationNotFoundException("Brak wskazanej lokalizacji");
      }
      return UserLocationDtoMapper.getUserLocationDtoList(userLocationResponse);
    } catch (RestClientException e) {
      throw new UserLocationApiException("Błąd połączenia", e);
    }
  }

}
