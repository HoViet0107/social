package personal.social.model;

import jakarta.persistence.*;
import lombok.*;
import personal.social.enums.ObjectType;
import personal.social.enums.Status;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "feed_item", indexes = {
        @Index(name = "idx_parent_item", columnList = "parent_item_id"),
        @Index(name = "idx_item_type", columnList = "item_type")
})
public class FeedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_item_id")
    private Long feedItemId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "f_status")
    private Status fstatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type")
    private ObjectType itemType; // POST, COMMENT, REPLY

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "parent_item_id")
    private Long parentFItem; // null if its post; not null if its comment or reply

    public FeedItem(LocalDateTime createdAt, LocalDateTime updatedAt, Status fstatus, ObjectType itemType, Users user, Long parentFItem) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fstatus = fstatus;
        this.itemType = itemType;
        this.user = user;
        this.parentFItem = parentFItem;
    }
}
