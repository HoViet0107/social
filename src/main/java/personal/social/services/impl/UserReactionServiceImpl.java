package personal.social.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.social.dto.ReactionRequest;
import personal.social.model.ReactionSummary;
import personal.social.model.User;
import personal.social.model.UserReaction;
import personal.social.repository.ReactionSummaryRepository;
import personal.social.repository.UserReactionRepository;
import personal.social.services.UserReactionService;

@Service
public class UserReactionServiceImpl implements UserReactionService {
    private final ReactionSummaryRepository reactSumRepos;
    private final UserReactionRepository userReactionRepos;

    @Autowired
    public UserReactionServiceImpl(ReactionSummaryRepository reactSumRepos, UserReactionRepository userReactionRepos) {
        this.reactSumRepos = reactSumRepos;
        this.userReactionRepos = userReactionRepos;
    }

    @Override
    public UserReaction userReaction(ReactionRequest reactionRequest, User user) {
        try {
            // Find existed post reaction
            ReactionSummary existedSumReaction = reactSumRepos.findByObjectTypeAndObjectId(
                    reactionRequest.getObjectType(),
                    reactionRequest.getObjectId());

            if (existedSumReaction == null) { // Save new if its null
                throw new RuntimeException("Post reaction summary not existed!");
            } else{
                // Call the stored procedure
                reactSumRepos.toggleReaction(
                        user.getUserId(),
                        reactionRequest.getObjectType().toString(),
                        reactionRequest.getObjectId(),
                        reactionRequest.getReactionType().toString());

                // After toggling the reaction, set new reaction summary
                return userReactionRepos.findUserReaction(
                        reactionRequest.getObjectType(),
                        reactionRequest.getObjectId(),
                        reactionRequest.getReactionType(),
                        user.getUserId()
                );
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to like/unlike post: " + e.getMessage());
        }
    }
}
