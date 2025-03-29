package pl.mateusz.example.friendoo.user.activation;

import lombok.Data;

/**
 * Data transfer object representing a user activation token.
 */
@Data
public class UserActivationTokenDto {

  private String userEmail;
  private String token;
}
