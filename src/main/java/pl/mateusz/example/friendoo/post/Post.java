package pl.mateusz.example.friendoo.post;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
//Post to jest wiadomość napisana na tablicy strony
public abstract class Post {

    private LocalDateTime createdAt;

    private String content;

}
