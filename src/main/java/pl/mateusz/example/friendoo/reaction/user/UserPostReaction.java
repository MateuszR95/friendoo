package pl.mateusz.example.friendoo.reaction.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.user.UserPostComment;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.Reaction;
import pl.mateusz.example.friendoo.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPostReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "user_post_id")
    private UserPost post;


    @ManyToOne
    @JoinColumn(name = "reaction_id")
    private Reaction reaction;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private UserPostComment comment;


    private LocalDateTime reactionTime;


}
