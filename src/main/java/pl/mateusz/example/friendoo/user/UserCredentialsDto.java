package pl.mateusz.example.friendoo.user;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Getter
@Setter
@AllArgsConstructor
public class UserCredentialsDto {

  private String email;
  private String password;
  private Set<String> roles = new HashSet<>();
}
