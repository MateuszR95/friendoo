package pl.mateusz.example.friendoo.user.location;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository for managing user addresses.
 */
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

  Optional<UserAddress> findByCityAndTownAndVillageAndStateAndMunicipalityAndCountry(String city,
        String town, String village, String state, String municipality, String country);
}
