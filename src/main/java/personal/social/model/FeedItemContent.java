package personal.social.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FeedItemContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "f_item_id")
    private Long fItemId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // feed item content

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "feed_item_id", nullable = false)
    private FeedItem feedItem;

    public FeedItemContent(String content, LocalDateTime updatedAt, FeedItem feedItem) {
        this.content = content;
        this.updatedAt = updatedAt;
        this.feedItem = feedItem;
    }
}
