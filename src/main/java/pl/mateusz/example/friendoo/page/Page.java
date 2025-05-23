package pl.mateusz.example.friendoo.page;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.PostComment;
import pl.mateusz.example.friendoo.page.category.PageCategory;
import pl.mateusz.example.friendoo.photo.Photo;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.reaction.PageLike;
import pl.mateusz.example.friendoo.reaction.PostReaction;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.visit.PageVisit;

/**
 * Entity representing a page.
 */
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
  private Set<Photo> photos = new HashSet<>();

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
  private Photo profilePicture;

  @OneToOne
  @JoinColumn(name = "background_photo_id")
  private Photo backgroundPhoto;

  @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
  private Set<PagePost> pagePosts = new HashSet<>();

  @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
  private Set<PageLike> pageLikes = new HashSet<>();

  @OneToMany(mappedBy = "pageAuthor", cascade = CascadeType.ALL)
  private Set<PostReaction> postReactions = new HashSet<>();

  @OneToMany(mappedBy = "pageAuthor", cascade = CascadeType.ALL)
  private Set<PostComment> postComments = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "page_category_id")
  private PageCategory pageCategory;

  @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
  private Set<PageVisit> pageVisits = new HashSet<>();

}
