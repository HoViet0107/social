package personal.social.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_content", indexes = {
        @Index(name = "idx_comment_content_comment", columnList = "comment_id")
})
public class CommentContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_content_id")
    private Integer commentContentId;

    @Column(columnDefinition = "VARCHAR(5000) NOT NULL")
    private String content;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    public CommentContent(){}

    public CommentContent(String content, LocalDateTime lastUpdated, Comment comment) {
        this.content = content;
        this.lastUpdated = lastUpdated;
        this.comment = comment;
    }

    public CommentContent(Integer commentContentId, String content, LocalDateTime lastUpdated, Comment comment) {
        this.commentContentId = commentContentId;
        this.content = content;
        this.lastUpdated = lastUpdated;
        this.comment = comment;
    }

    public Integer getCommentContentId() {
        return commentContentId;
    }

    public void setCommentContentId(Integer commentContentId) {
        this.commentContentId = commentContentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
