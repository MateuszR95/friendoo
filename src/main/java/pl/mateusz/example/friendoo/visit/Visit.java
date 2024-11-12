package pl.mateusz.example.friendoo.visit;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Visit {

  private LocalDateTime visitedAt;
}
