package pl.mateusz.example.friendoo.comment.user;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.PostCommentReaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Mapper class for converting between PostComment and PostCommentDto objects.
 */
public class PostCommentDtoMapper {

  private static final int SECONDS_IN_MINUTE = 60;
  private static final int HOURS_IN_DAY = 24;
  private static final int MONTHS_IN_YEAR = 12;

  /**
   * Converts a PostComment entity to a PostCommentDto.
   *
   * @param postComment the PostComment entity to convert
   * @return the converted PostCommentDto
   */
  public static PostCommentDto mapToDto(PostComment postComment) {
    return PostCommentDto.builder()
        .id(postComment.getId())
        .createdAt(postComment.getCreatedAt())
        .content(postComment.getContent())
        .userAuthorId(Optional.ofNullable(postComment.getAuthor())
          .map(User::getId)
          .orElse(null))
        .pageAuthorId(Optional.ofNullable(postComment.getPageAuthor())
          .map(Page::getId)
          .orElse(null))
        .userPostId(Optional.ofNullable(postComment.getUserPost())
          .map(UserPost::getId)
          .orElse(null))
        .pagePostId(Optional.ofNullable(postComment.getPagePost())
          .map(PagePost::getId)
          .orElse(null))
        .creationDateDescription(formatCommentDateToDescription(postComment.getCreatedAt()))
        .reactionsCount(postComment.getReactions().size())
        .build();
  }

  /**
   * Converts a PostCommentDto to a PostComment entity.
   *
   * @param dto the PostCommentDto to convert
   * @param userPost the UserPost associated with the comment
   * @param user the User who authored the comment
   * @param userPostCommentReactions the reactions associated with the comment
   * @return the converted PostComment entity
   */
  public static PostComment mapToEntity(PostCommentDto dto,
                                        UserPost userPost, User user,
                                        Set<PostCommentReaction> userPostCommentReactions) {
    PostComment postComment = new PostComment();
    postComment.setId(dto.getId());
    postComment.setContent(dto.getContent());
    postComment.setCreatedAt(dto.getCreatedAt());
    postComment.setUserPost(userPost);
    postComment.setAuthor(user);
    postComment.setReactions(userPostCommentReactions);
    return postComment;
  }

  private static String formatCommentDateToDescription(LocalDateTime createdAt) {
    LocalDateTime now = LocalDateTime.now();
    long seconds = ChronoUnit.SECONDS.between(createdAt, now);
    if (seconds == 1) {
      return "1 sekundę temu";
    } else if (seconds >= 2 && seconds <= 4) {
      return seconds + " sekundy temu";
    } else if (seconds >= 5 && seconds < SECONDS_IN_MINUTE) {
      return seconds + " sekund temu";
    }

    long minutes = ChronoUnit.MINUTES.between(createdAt, now);
    if (minutes == 1) {
      return "1 minutę temu";
    } else if (minutes >= 2 && minutes <= 4) {
      return minutes + " minuty temu";
    } else if (minutes >= 5 && minutes < SECONDS_IN_MINUTE) {
      return minutes + " minut temu";
    }

    long hours = ChronoUnit.HOURS.between(createdAt, now);
    if (hours < HOURS_IN_DAY) {
      return hours + " godzin temu";
    }

    long days = ChronoUnit.DAYS.between(createdAt, now);
    if (days == 1) {
      return "1 dzień temu";
    } else if (days >= 2 && days <= 7) {
      return days + " dni temu";
    }
    long weeks = ChronoUnit.WEEKS.between(createdAt, now);
    if (weeks == 1) {
      return "1 tydzień temu";
    } else if (weeks >= 2 && weeks <= 4) {
      return weeks + " tygodnie temu";
    }
    long months = ChronoUnit.MONTHS.between(createdAt, now);
    if (months == 1) {
      return "1 miesiąc temu";
    } else if (months >= 2 && months <= 4) {
      return months + " miesiące temu";
    } else if (months < MONTHS_IN_YEAR) {
      return months + " miesięcy temu";
    }
    long years = ChronoUnit.YEARS.between(createdAt, now);
    if (years == 1) {
      return "1 rok temu";
    } else if (years >= 2 && years <= 4) {
      return years + " lata temu";
    } else {
      return years + " lat temu";
    }
  }

}
