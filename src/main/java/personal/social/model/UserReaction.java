package personal.social.model;

import jakarta.persistence.*;
import personal.social.enums.ObjectType;
import personal.social.enums.ReactionType;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_reaction", indexes = {
        @Index(name = "idx_user_object", columnList = "user_id, object_type, object_id")
})
public class UserReaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType reactionType; // LIKE, DISLIKE, REPORT, SHARE

    @Enumerated(EnumType.STRING)
    @Column(name = "object_type")
    private ObjectType objectType; // POST, COMMENT

    @Column(name = "object_id")
    private Long objectId; // ID post or comment

    @Column(name = "reacted_at")
    private LocalDateTime reactedAt;

    public UserReaction() {
    }

    public UserReaction(Long id, User user, ReactionType reactionType, ObjectType objectType, Long objectId, LocalDateTime reactedAt) {
        this.id = id;
        this.user = user;
        this.reactionType = reactionType;
        this.objectType = objectType;
        this.objectId = objectId;
        this.reactedAt = reactedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public LocalDateTime getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(LocalDateTime reactedAt) {
        this.reactedAt = reactedAt;
    }
}
