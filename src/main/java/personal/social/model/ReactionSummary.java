package personal.social.model;

import jakarta.persistence.*;
import personal.social.enums.ObjectType;

@NamedStoredProcedureQuery(
        name = "ReactionSummary.toggleReaction",
        procedureName = "ToggleReaction",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_object_type", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_object_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_reaction_type", type = String.class)
        }
)
@Entity
@Table(name = "reaction_aggregate", indexes = {
        @Index(name = "idx_aggregate_object", columnList = "object_type, object_id")
})
public class ReactionSummary {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "object_type")
    private ObjectType objectType; // POST, COMMENT

    @Column(name = "object_id")
    private Long objectId; // ID post or comment

    private Long likes = 0L;
    private Long dislikes = 0L;
    private Long reports = 0L;
    private Long shares = 0L;

    public ReactionSummary() {
    }

    public ReactionSummary(ObjectType objectType, Long objectId, Long likes, Long dislikes, Long reports, Long shares) {
        this.objectType = objectType;
        this.objectId = objectId;
        this.likes = likes;
        this.dislikes = dislikes;
        this.reports = reports;
        this.shares = shares;
    }

    public ReactionSummary(Long id, ObjectType objectType, Long objectId, Long likes, Long dislikes, Long reports, Long shares) {
        this.id = id;
        this.objectType = objectType;
        this.objectId = objectId;
        this.likes = likes;
        this.dislikes = dislikes;
        this.reports = reports;
        this.shares = shares;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getDislikes() {
        return dislikes;
    }

    public void setDislikes(Long dislikes) {
        this.dislikes = dislikes;
    }

    public Long getReports() {
        return reports;
    }

    public void setReports(Long reports) {
        this.reports = reports;
    }

    public Long getShares() {
        return shares;
    }

    public void setShares(Long shares) {
        this.shares = shares;
    }
}

