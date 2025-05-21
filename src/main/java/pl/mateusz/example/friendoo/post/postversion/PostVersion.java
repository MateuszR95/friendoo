package pl.mateusz.example.friendoo.post.postversion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.user.UserPost;

import java.time.LocalDateTime;

/**
 * Entity representing a version of a post.
 * This class is used to track different versions of a post.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PostVersion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_post_id")
  private UserPost userPost;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "page_post_id")
  private PagePost pagePost;
  @Column(nullable = false)
  private Long editorUserId;
  @Column(nullable = false)
  private String content;
  @Column(nullable = false)
  private LocalDateTime createdAt;

}
