package personal.social.dto;

import lombok.Data;

@Data
public class EditFeedItemRequest {
    private FeedItemDTO feedItemDTO;
    private ReactionRequest reactionRequest;
}
