package personal.social.model;

import jakarta.persistence.*;
import personal.social.enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comment_post", columnList = "post_id"),
        @Index(name = "idx_comment_parent", columnList = "parent_comment_id")
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_status")
    private Status commentStatus;

    private Integer likes=0;

    private Integer dislikes=0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // Self-referencing to identify which comment reply to
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    public Comment(){}

    public Comment(Integer commentId, LocalDateTime createdAt, LocalDateTime lastUpdated, Status commentStatus, Integer likes, Integer dislikes, User user, Post post, Comment parentComment) {
        this.commentId = commentId;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.commentStatus = commentStatus;
        this.likes = likes;
        this.dislikes = dislikes;
        this.user = user;
        this.post = post;
        this.parentComment = parentComment;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Status getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Status commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }
}
