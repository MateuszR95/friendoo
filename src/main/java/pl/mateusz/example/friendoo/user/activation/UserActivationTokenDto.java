package pl.mateusz.example.friendoo.user.activation;

import lombok.Data;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
public class UserActivationTokenDto {

  private String userEmail;
  private String token;
}
