package pl.mateusz.example.friendoo.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
//komentarz do zdjęcia/postu
public abstract class Comment {

    private LocalDateTime createdAt;

    private String content;


}
