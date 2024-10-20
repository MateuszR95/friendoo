package pl.mateusz.example.friendoo.comment.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.Comment;
import pl.mateusz.example.friendoo.photo.PagePhoto;
import pl.mateusz.example.friendoo.photo.UserPhoto;
import pl.mateusz.example.friendoo.reaction.user.UserPhotoReaction;
import pl.mateusz.example.friendoo.reaction.user.UserPostReaction;
import pl.mateusz.example.friendoo.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPhotoComment extends Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "user_photo_id")
    private UserPhoto userPhoto;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<UserPhotoReaction> reactions = new HashSet<>();



}
