package pl.mateusz.example.friendoo.user.activation;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.user.User;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserActivationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  private String token;

  private LocalDateTime creationDate;

  private LocalDateTime expireDate;

}

