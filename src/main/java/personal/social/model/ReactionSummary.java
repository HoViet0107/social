package personal.social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
        @Index(name = "idx_aggregate_item", columnList = "item_type, feed_item_id")
})
@Data
@NoArgsConstructor @AllArgsConstructor
public class ReactionSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type")
    private ObjectType itemType; // POST, COMMENT

    @OneToOne
    @Column(name = "feed_item_id")
    private FeedItem feedItem; // ID post or comment

    private Long likes = 0L;
    private Long dislikes = 0L;
    private Long reports = 0L;
    private Long shares = 0L;

    public ReactionSummary(ObjectType itemType, FeedItem feedItem, Long likes, Long dislikes, Long reports, Long shares) {
        this.itemType = itemType;
        this.feedItem = feedItem;
        this.likes = likes;
        this.dislikes = dislikes;
        this.reports = reports;
        this.shares = shares;
    }
}

