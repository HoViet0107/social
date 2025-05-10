package personal.social.services;

import org.springframework.data.domain.Page;
import personal.social.dto.FeedItemDTO;
import personal.social.dto.ReactionInfoDTO;
import personal.social.dto.ReactionRequest;
import personal.social.enums.ObjectType;
import personal.social.model.Users;

import java.nio.file.AccessDeniedException;

public interface FeedItemService {
    Page<FeedItemDTO> getAllFeedItem(Integer pageNumber, Integer pageSize, ObjectType fItemType) throws Exception;

    ReactionInfoDTO getFeedItemReactionInfo(Long feedItemId);

    ReactionInfoDTO createFeedItem (FeedItemDTO feedItemDTO, Users user);

    String editFeedItem(FeedItemDTO feedItemDTO, ReactionRequest reactionRequest, Users user) throws AccessDeniedException;
}
