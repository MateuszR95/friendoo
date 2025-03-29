package pl.mateusz.example.friendoo.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for displaying user data.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDisplayDto {

  private Long id;
  private String firstName;
  private String lastName;
}
