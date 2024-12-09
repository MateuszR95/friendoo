package pl.mateusz.example.friendoo.user.location;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserLocationDtoMapper {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  private static UserLocationDto mapToUserLocationDto(UserLocation userLocation) {
    UserLocationDto userLocationDto = new UserLocationDto();
    userLocationDto.setPostalCode(userLocation.getPostalCode());
    userLocationDto.setName(userLocation.getPlaceName());
    userLocationDto.setCountryCode(userLocation.getCountryCode());
    userLocationDto.setState(userLocation.getAdminName3());
    userLocationDto.setMunicipality(userLocation.getAdminName1());
    return userLocationDto;
  }

  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public static List<UserLocationDto> getUserLocationDtoList(UserLocationResponse
                                                                 userLocationResponse) {
    return userLocationResponse.getPostalCodes()
      .stream()
      .map(UserLocationDtoMapper::mapToUserLocationDto)
      .filter(distinctByKey(UserLocationDto::getName))
      .collect(Collectors.toList());
  }
}
