package pl.mateusz.example.friendoo.photo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public abstract class Photo {

    private String photoUrl;
    private LocalDateTime photoUploadedAt;
    private String description;




}
