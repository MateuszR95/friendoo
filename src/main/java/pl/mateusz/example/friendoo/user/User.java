package pl.mateusz.example.friendoo.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.user.UserPhotoComment;
import pl.mateusz.example.friendoo.comment.user.UserPostComment;
import pl.mateusz.example.friendoo.gender.UserGender;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.photo.UserPhoto;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.user.UserPhotoReaction;
import pl.mateusz.example.friendoo.reaction.user.UserPostReaction;
import pl.mateusz.example.friendoo.user.role.UserRole;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private LocalDate dateOfBirth;
  private String bio;
  private String city;
  private String phoneNumber;
  @ManyToOne
  @JoinColumn(name = "user_gender_id")
  private UserGender gender;
  private LocalDateTime joinedAt;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @JoinTable(name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
  )
  private Set<UserRole> roles = new HashSet<>();


  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_friends",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "friend_id")
  )
  private Set<User> friends = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private Set<UserPost> posts = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserPostReaction> postCommentReactions = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private Set<UserPostComment> postComments = new HashSet<>();

  @OneToOne
  @JoinColumn(name = "profile_photo_id")
  private UserPhoto profilePicture;

  @OneToOne
  @JoinColumn(name = "background_photo_id")
  private UserPhoto backgroundPhoto;

  @OneToMany(mappedBy = "user")
  private List<UserPhoto> photos = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserPhotoReaction> photoReactions = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private Set<UserPhotoComment> photoComments = new HashSet<>();

  @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
  private Set<Page> createdPages = new HashSet<>();

  @ManyToMany
  @JoinTable(
      name = "user_likes",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "page_id")
  )
  private Set<Page> likedPages = new HashSet<>();


}
