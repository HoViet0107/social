package personal.social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import personal.social.enums.ObjectType;
import personal.social.enums.ReactionType;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_reaction", indexes = {
        @Index(name = "idx_user_object", columnList = "user_id, object_type, object_id")
})
@Data
@AllArgsConstructor @NoArgsConstructor
public class UserReaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType reactionType; // LIKE, DISLIKE, REPORT, SHARE

    @Enumerated(EnumType.STRING)
    @Column(name = "object_type")
    private ObjectType objectType; // POST, COMMENT

    @ManyToOne
    @Column(name = "feed_item_id")
    private FeedItem feedItem; // ID post or comment

    @Column(name = "reacted_at")
    private LocalDateTime reactedAt;
}
