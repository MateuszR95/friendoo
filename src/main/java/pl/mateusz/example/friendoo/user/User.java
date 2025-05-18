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
import pl.mateusz.example.friendoo.comment.PhotoComment;
import pl.mateusz.example.friendoo.comment.PostComment;
import pl.mateusz.example.friendoo.gender.UserGender;
import pl.mateusz.example.friendoo.invitation.InvitationToFriendship;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.photo.Photo;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.PageLike;
import pl.mateusz.example.friendoo.reaction.PhotoReaction;
import pl.mateusz.example.friendoo.reaction.PostCommentReaction;
import pl.mateusz.example.friendoo.reaction.PostReaction;
import pl.mateusz.example.friendoo.user.favouritepagecategory.UserFavouritePageCategory;
import pl.mateusz.example.friendoo.user.location.UserAddress;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetToken;
import pl.mateusz.example.friendoo.user.role.UserRole;
import pl.mateusz.example.friendoo.visit.PageVisit;
import pl.mateusz.example.friendoo.visit.UserProfileVisit;

/**
 * Entity representing a user.
 */
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
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hometown_id")
  private UserAddress hometown;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "current_city_id")
  private UserAddress currentCity;
  private String phoneNumber;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_gender_id")
  private UserGender gender;
  private LocalDateTime joinedAt;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
  )
  private Set<UserRole> roles = new HashSet<>();

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private Set<InvitationToFriendship> sentFriendRequests = new HashSet<>();

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private Set<InvitationToFriendship> receivedFriendRequests = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_friends",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "friend_id")
  )
  private Set<User> friends = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private Set<UserPost> posts = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private Set<PostReaction> postCommentReactions = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private Set<PostComment> postComments = new HashSet<>();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_photo_id")
  private Photo profilePicture;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "background_photo_id")
  private Photo backgroundPhoto;

  @OneToMany(mappedBy = "user")
  private List<Photo> photos = new ArrayList<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private Set<PhotoReaction> photoReactions = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private Set<PhotoComment> photoComments = new HashSet<>();

  @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
  private Set<Page> createdPages = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<PageLike> pageLikes = new HashSet<>();

  @OneToMany(mappedBy = "viewer", cascade = CascadeType.ALL)
  private Set<UserProfileVisit> profileVisits = new HashSet<>();

  @OneToMany(mappedBy = "viewer", cascade = CascadeType.ALL)
  private Set<PageVisit> pageVisits = new HashSet<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private Set<PostCommentReaction> userPostCommentReactions = new HashSet<>();

  private boolean isActiveAccount;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserPasswordResetToken> passwordResetTokens = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserFavouritePageCategory> favouritePageCategories = new HashSet<>();


}
