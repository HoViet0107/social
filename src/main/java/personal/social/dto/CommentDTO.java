package personal.social.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import personal.social.enums.Status;
import personal.social.model.Comment;
import personal.social.model.Post;
import personal.social.model.User;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long commentId;

    @JsonIgnore // use JsonIgnore to show create time only on response
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime lastUpdated;

    @JsonIgnore
    private Status commentStatus;

    private Long likes;

    private Long dislikes;

    private String content;

    private Long userId;

    private Long postId;

    private Long parentCommentId;

    public CommentDTO(){}


    public CommentDTO(Long commentId, LocalDateTime createdAt,LocalDateTime lastUpdated, Status commentStatus, Long likes, Long dislikes, String content, Long userId, Long postId, Long parentCommentId) {
        this.commentId = commentId;
        this.createdAt = createdAt;
        this.commentStatus = commentStatus;
        this.lastUpdated = lastUpdated;
        this.likes = likes;
        this.dislikes = dislikes;
        this.content = content;
        this.userId = userId;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
    }

    public CommentDTO(Long commentId, Long likes, Long dislikes, String content, Long userId, Long postId, Long parentCommentId) {
        this.commentId = commentId;
        this.likes = likes;
        this.dislikes = dislikes;
        this.content = content;
        this.userId = userId;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Status getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Status commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Long getLikes() {
        return likes;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
