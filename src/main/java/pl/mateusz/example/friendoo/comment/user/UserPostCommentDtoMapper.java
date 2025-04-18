package pl.mateusz.example.friendoo.comment.user;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.user.UserPostCommentReaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Mapper class for converting UserPostComment entities to UserPostCommentDto objects.
 * This class provides a utility method to map the data from the UserPostComment entity
 * to the UserPostCommentDto, which is used for data transfer purposes.
 */
public class UserPostCommentDtoMapper {

  private static final int SECONDS_IN_MINUTE = 60;
  private static final int HOURS_IN_DAY = 24;
  private static final int MONTHS_IN_YEAR = 12;

  /**
   * Maps a UserPostComment entity to a UserPostCommentDto.
   *
   * @param userPostComment the UserPostComment entity
   * @return the mapped UserPostCommentDto
   */

  public static UserPostCommentDto mapToDto(UserPostComment userPostComment) {
    return UserPostCommentDto.builder()
      .id(userPostComment.getId())
      .authorId(userPostComment.getAuthor().getId())
      .postId(userPostComment.getPost().getId())
      .createdAt(userPostComment.getCreatedAt())
      .creationDateDescription(formatCommentDateToDescription(userPostComment.getCreatedAt()))
      .content(userPostComment.getContent())
      .reactionsCount(userPostComment.getReactions().size())
      .build();
  }

  /**
   * Maps a UserPostCommentDto to a UserPostComment entity.
   *
   * @param userPostCommentDto the UserPostCommentDto
   * @return the mapped UserPostComment entity
   */
  public static UserPostComment mapToEntity(UserPostCommentDto userPostCommentDto,
                                            UserPost userPost, User user,
                                            Set<UserPostCommentReaction> userPostCommentReactions) {
    UserPostComment userPostComment = new UserPostComment();
    userPostComment.setId(userPostCommentDto.getId());
    userPostComment.setContent(userPostCommentDto.getContent());
    userPostComment.setCreatedAt(userPostCommentDto.getCreatedAt());
    userPostComment.setPost(userPost);
    userPostComment.setAuthor(user);
    userPostComment.setReactions(userPostCommentReactions);
    return userPostComment;
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
