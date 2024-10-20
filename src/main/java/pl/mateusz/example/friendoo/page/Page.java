package pl.mateusz.example.friendoo.page;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.photo.PagePhoto;
import pl.mateusz.example.friendoo.photo.UserPhoto;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private Set<PagePhoto> photos = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @ManyToMany
    @JoinTable(
            name = "page_admins",
            joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> pageAdmins = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "profile_photo_id")
    private PagePhoto profilePicture;

    @OneToOne
    @JoinColumn(name = "background_photo_id")
    private PagePhoto backgroundPhoto;


    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private Set<PagePost> pagePosts = new HashSet<>();

    @ManyToMany(mappedBy = "likedPages")
    private Set<User> usersWhoLiked = new HashSet<>();


}
