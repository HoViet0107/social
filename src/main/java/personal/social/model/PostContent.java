package personal.social.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class PostContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postContentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // post content

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostContent() {
    }

    public PostContent(Integer postContentId, String content, LocalDateTime updatedAt, Post post) {
        this.postContentId = postContentId;
        this.content = content;
        this.updatedAt = updatedAt;
        this.post = post;
    }

    public Integer getPostContentId() {
        return postContentId;
    }

    public void setPostContentId(Integer postContentId) {
        this.postContentId = postContentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
