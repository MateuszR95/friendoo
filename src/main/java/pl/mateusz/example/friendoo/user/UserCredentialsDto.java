package pl.mateusz.example.friendoo.user;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCredentialsDto {

  private String email;
  private String password;
  private Set<String> roles = new HashSet<>();
  private boolean isActiveAccount;
}
