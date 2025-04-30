package personal.social.services;

import personal.social.dto.ReactionRequest;
import personal.social.model.User;
import personal.social.model.UserReaction;

public interface UserReactionService {
    UserReaction userReaction(ReactionRequest reactionRequest, User user);
}
