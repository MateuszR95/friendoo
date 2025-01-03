package pl.mateusz.example.friendoo.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("checkstyle:MissingJavadocType")
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class UserToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  private LocalDateTime creationDate;

  private LocalDateTime expireDate;
}
