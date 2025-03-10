package personal.social.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class PostContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer post_content_id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // post content

    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Integer getPost_content_id() {
        return post_content_id;
    }

    public void setPost_content_id(Integer post_content_id) {
        this.post_content_id = post_content_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public PostContent(Integer post_content_id, String content, LocalDateTime updated_at, Post post) {
        this.post_content_id = post_content_id;
        this.content = content;
        this.updated_at = updated_at;
        this.post = post;
    }

    public PostContent() {
    }
}
