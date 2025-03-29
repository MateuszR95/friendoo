package pl.mateusz.example.friendoo.gender;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the UserGender entity.
 */
public interface UserGenderRepository extends JpaRepository<UserGender, Long> {

  Optional<UserGender> getUserGenderByGender(Gender gender);
}
