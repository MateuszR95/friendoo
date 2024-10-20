package pl.mateusz.example.friendoo.post.page;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.page.PagePostComment;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.post.Post;
import pl.mateusz.example.friendoo.reaction.page.PagePostReaction;
import pl.mateusz.example.friendoo.user.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PagePost extends Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page page;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<PagePostReaction> reactions = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<PagePostComment> comments = new HashSet<>();
}
