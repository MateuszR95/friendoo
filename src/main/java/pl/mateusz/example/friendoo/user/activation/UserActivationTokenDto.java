package pl.mateusz.example.friendoo.user.activation;

import java.time.LocalDateTime;
import lombok.Data;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
public class UserActivationTokenDto {

  private String userEmail;
  private String token;
}
