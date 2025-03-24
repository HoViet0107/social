package personal.social.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import personal.social.enums.Status;

import java.time.LocalDateTime;

public class CommentDTO {
    private Integer commentId;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private Status commentStatus;

    @JsonIgnore
    private LocalDateTime lastUpdated;

    private Integer likes;

    private Integer dislikes;

    private String content;

    private Integer userId;

    private Integer postId;

    private Integer parentCommentId;

    public CommentDTO(){}

    public CommentDTO(Integer commentId, LocalDateTime createdAt, LocalDateTime lastUpdated, Status commentStatus, Integer likes, Integer dislikes, String content, Integer userId, Integer postId, Integer parentCommentId) {
        this.commentId = commentId;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.commentStatus = commentStatus;
        this.likes = likes;
        this.dislikes = dislikes;
        this.content = content;
        this.userId = userId;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
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

    public Status getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Status commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Integer getLikes() {
        return likes;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
