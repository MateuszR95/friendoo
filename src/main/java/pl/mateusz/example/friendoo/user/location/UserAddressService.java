package pl.mateusz.example.friendoo.user.location;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.user.User;

/**
 * Service for managing user addresses.
 */
@Service
public class UserAddressService {

  private final UserAddressRepository userAddressRepository;

  public UserAddressService(UserAddressRepository userAddressRepository) {
    this.userAddressRepository = userAddressRepository;
  }

  /**
   * Assigns addresses to a user.
   *
   * @param currentCity the current city
   * @param user the user
   * @param hometown the hometown
   */
  @Transactional
  public void assignAddressesToUser(UserAddress currentCity, User user, UserAddress hometown) {
    Optional<UserAddress> currentCityOptional = userAddressRepository
        .findByCityAndTownAndVillageAndStateAndMunicipalityAndCountry(
        currentCity.getCity(), currentCity.getTown(), currentCity.getVillage(),
          currentCity.getState(), currentCity.getMunicipality(), currentCity.getCountry());
    if (currentCityOptional.isEmpty()) {
      UserAddress savedCurrentCity = userAddressRepository.save(currentCity);
      user.setCurrentCity(savedCurrentCity);
    } else {
      user.setCurrentCity(currentCityOptional.get());
    }
    Optional<UserAddress> hometownOptional = userAddressRepository
        .findByCityAndTownAndVillageAndStateAndMunicipalityAndCountry(
        hometown.getCity(), hometown.getTown(), hometown.getVillage(), hometown.getState(),
        hometown.getMunicipality(), hometown.getCountry());
    if (hometownOptional.isEmpty()) {
      UserAddress savedHometown = userAddressRepository.save(hometown);
      user.setHometown(savedHometown);
    } else {
      user.setHometown(hometownOptional.get());
    }
  }
}
