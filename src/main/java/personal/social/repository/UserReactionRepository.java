package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import personal.social.enums.ObjectType;
import personal.social.enums.ReactionType;
import personal.social.model.UserReaction;

public interface UserReactionRepository extends JpaRepository<UserReaction, Long> {
//    @Query(value = "SELECT new UserReaction(ur.id, ur.user, ur.reactionType, ur.objectType, ur.objectId, ur.reactedAt) " +
//            "FROM UserReaction ur " +
//            "WHERE ur.objectType =:objectType " +
//            "AND ur.objectId=:objectId " +
//            "AND ur.reactionType=:reactionType " +
//            "AND ur.user.userId=:userId ")
//    UserReaction findUserReaction(
//            ObjectType objectType,
//            Long objectId,
//            ReactionType reactionType,
//            Long userId
//    );

    @Query(value = "SELECT ur FROM UserReaction ur " +
            "WHERE ur.feedItem.feedItemId =:feedItemId " +
            "AND ur.reactionType =:reactionType")
    UserReaction findByFeedItemId (Long feedItemId, ReactionType reactionType);
}
