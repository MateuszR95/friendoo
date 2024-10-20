package pl.mateusz.example.friendoo.photo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.page.PagePhotoComment;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.reaction.page.PagePhotoReaction;
import pl.mateusz.example.friendoo.user.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PagePhoto extends Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page page;

    @OneToMany(mappedBy = "pagePhoto", cascade = CascadeType.ALL)
    private Set<PagePhotoReaction> reactions = new HashSet<>();

    @OneToMany(mappedBy = "pagePhoto", cascade = CascadeType.ALL)
    private Set<PagePhotoComment> comments = new HashSet<>();



}
