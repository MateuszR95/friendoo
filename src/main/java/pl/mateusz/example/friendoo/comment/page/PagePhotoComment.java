package pl.mateusz.example.friendoo.comment.page;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.Comment;
import pl.mateusz.example.friendoo.photo.PagePhoto;
import pl.mateusz.example.friendoo.reaction.page.PagePhotoReaction;
import pl.mateusz.example.friendoo.reaction.user.UserPhotoReaction;
import pl.mateusz.example.friendoo.user.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PagePhotoComment extends Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "page_photo_id")
    private PagePhoto pagePhoto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<PagePhotoReaction> reactions = new HashSet<>();

}
