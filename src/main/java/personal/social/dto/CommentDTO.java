package personal.social.dto;

import personal.social.enums.Status;

import java.time.LocalDateTime;

public class CommentDTO {
    private Integer commentId;
    private LocalDateTime createdAt;
    private Status commentStatus;
    private Integer likes;
    private Integer dislikes;
    private String content;
    private Integer userId;
    private Integer postId;
    private Integer parentCommentId;
}
