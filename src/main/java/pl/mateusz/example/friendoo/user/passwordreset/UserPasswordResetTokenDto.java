package pl.mateusz.example.friendoo.user.passwordreset;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Data
@NoArgsConstructor
public class UserPasswordResetTokenDto {

  private String userEmail;

  private String tokenCode;
}
