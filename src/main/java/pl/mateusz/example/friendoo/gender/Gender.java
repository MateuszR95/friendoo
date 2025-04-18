package pl.mateusz.example.friendoo.gender;

import lombok.Getter;
import lombok.Setter;

/**
 * Enum representing the gender of a person.
 */
@Getter
public enum Gender {

    MAN("Mężczyzna"),
    WOMAN("Kobieta"),
    OTHER("Inna");

  private final String plName;

  Gender(String plName) {
    this.plName = plName;
  }

}
