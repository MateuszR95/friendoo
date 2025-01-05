package pl.mateusz.example.friendoo.user.activation;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserToken;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserActivationToken extends UserToken {

  @OneToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;
}

