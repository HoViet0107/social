package personal.social.dto;


import personal.social.enums.ObjectType;
import personal.social.enums.ReactionType;

public class ReactionRequest {
    // take token from request to get user id
    private ObjectType objectType;
    private ReactionType reactionType;
    private Long objectId;

    public ReactionRequest() {  }

    public ReactionRequest(ObjectType objectType, ReactionType reactionType, Long objectId) {
        this.objectType = objectType;
        this.reactionType = reactionType;
        this.objectId = objectId;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
}
