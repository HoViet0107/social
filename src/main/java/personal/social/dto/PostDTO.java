package personal.social.dto;

import personal.social.enums.Status;

import java.time.LocalDateTime;

public class PostDTO {
    private Integer postId;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdated;

    private Status status;

    private String content;

    private Integer userId;

    public PostDTO(){}
    public PostDTO(Integer postId, LocalDateTime createdAt, LocalDateTime lastUpdated, Status status, String content, Integer userId) {
        this.postId = postId;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.status = status;
        this.content = content;
        this.userId = userId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
}
