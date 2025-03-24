package personal.social.dto;

import personal.social.enums.Status;

import java.time.LocalDateTime;

public class PostDTO {
    private Long postId;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdated;

    private Status status;

    private String content;

    private Long userId;

    public PostDTO(){}
    public PostDTO(Long postId, LocalDateTime createdAt, LocalDateTime lastUpdated, Status status, String content, Long userId) {
        this.postId = postId;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.status = status;
        this.content = content;
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
