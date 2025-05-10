package personal.social.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import personal.social.enums.ObjectType;
import personal.social.enums.ReactionType;
import personal.social.model.ReactionSummary;
import personal.social.model.UserReaction;

public interface ReactionSummaryRepository extends JpaRepository<ReactionSummary, Long> {
    // find post/comment reaction summary
    @Modifying
    @Transactional
    @Query(value = "CALL ToggleReaction(:p_user_id, :p_object_type, :p_object_id, :p_reaction_type)", nativeQuery = true)
    void toggleReaction(
            @Param("p_user_id") Long userId,
            @Param("p_object_type") String objectType,
            @Param("p_object_id") Long objectId,
            @Param("p_reaction_type") String reactionType
    );

//    ReactionSummary findByObjectTypeAndObjectId(ObjectType objectType, Long objectId);

    @Query(value = "SELECT rs FROM ReactionSummary rs WHERE rs.feedItem.feedItemId =:feedItemId")
    ReactionSummary findByFeedItemId(Long feedItemId);
}
