package pl.mateusz.example.friendoo.post;

import java.util.List;
import pl.mateusz.example.friendoo.reaction.PostReactionDto;

/**
 * Interface for mapping a post to a Data Transfer Object (DTO).
 * This interface defines the method for converting a post to its corresponding DTO representation.
 */
public interface MappablePost {

  PostDto toDto(List<PostReactionDto> reactions);
}
