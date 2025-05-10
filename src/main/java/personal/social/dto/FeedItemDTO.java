package personal.social.dto;

import personal.social.enums.ObjectType;
import personal.social.enums.Status;

import java.time.LocalDateTime;

public class FeedItemDTO {
    private Long feedItemId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Status fstatus;

    private ObjectType itemType;

    private String content;

    private Long userId;

    private Long parentFItem;

    public FeedItemDTO() {
    }

    public FeedItemDTO(Long feedItemId, LocalDateTime createdAt, LocalDateTime updatedAt, Status fstatus, ObjectType itemType, String content, Long userId, Long parentFItem) {
        this.feedItemId = feedItemId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fstatus = fstatus;
        this.itemType = itemType;
        this.content = content;
        this.userId = userId;
        this.parentFItem = parentFItem;
    }

    public Long getFeedItemId() {
        return feedItemId;
    }

    public void setFeedItemId(Long postId) {
        this.feedItemId = postId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Status getFstatus() {
        return fstatus;
    }

    public void setFstatus(Status fstatus) {
        this.fstatus = fstatus;
    }

    public ObjectType getItemType() {
        return itemType;
    }

    public void setItemType(ObjectType itemType) {
        this.itemType = itemType;
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

    public Long getParentFItem() {
        return parentFItem;
    }

    public void setParentFItem(Long parentFItem) {
        this.parentFItem = parentFItem;
    }
}
