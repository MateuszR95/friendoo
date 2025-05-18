package pl.mateusz.example.friendoo.post;

import java.util.List;
import java.util.Set;
import pl.mateusz.example.friendoo.comment.PostCommentDto;
import pl.mateusz.example.friendoo.reaction.PostReactionDto;

/**
 * Interface for mapping a post to a Data Transfer Object (DTO).
 * This interface defines the method for converting a post to its corresponding DTO representation.
 */
public interface MappablePost {

  PostDto toDto(Set<PostReactionDto> reactions, List<PostCommentDto> comments);
}
