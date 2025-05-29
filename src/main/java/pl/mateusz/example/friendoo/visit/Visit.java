package pl.mateusz.example.friendoo.visit;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract class for visit.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Visit {

  private LocalDateTime visitedAt;
}
