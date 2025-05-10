package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import personal.social.enums.ObjectType;
import personal.social.enums.ReactionType;
import personal.social.model.UserReaction;

/**
 * Repository interface for managing user reactions (likes, dislikes, etc.)
 * Provides methods to find reactions by feed item and reaction type
 */
public interface UserReactionRepository extends JpaRepository<UserReaction, Long> {
    /**
     * Finds a user reaction by feed item ID and reaction type
     * 
     * @param feedItemId The ID of the feed item
     * @param reactionType The type of reaction (LIKE, DISLIKE, etc.)
     * @return The user reaction if found
     */
    @Query(value = "SELECT ur FROM UserReaction ur " +
            "WHERE ur.feedItem.feedItemId =:feedItemId " +
            "AND ur.reactionType =:reactionType")
    UserReaction findByFeedItemId (Long feedItemId, ReactionType reactionType);
}
