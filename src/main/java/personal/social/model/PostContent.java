package personal.social.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PostContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_content_id")
    private Long postContentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // post content

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostContent() {
    }

    public PostContent(String content, LocalDateTime updatedAt, Post post) {
        this.content = content;
        this.updatedAt = updatedAt;
        this.post = post;
    }

    public PostContent(Long postContentId, String content, LocalDateTime updatedAt, Post post) {
        this.postContentId = postContentId;
        this.content = content;
        this.updatedAt = updatedAt;
        this.post = post;
    }

    public Long getPostContentId() {
        return postContentId;
    }

    public void setPostContentId(Long postContentId) {
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
