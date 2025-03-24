package personal.social.services;

import org.springframework.data.domain.Page;
import personal.social.dto.CommentDTO;

public interface CommentService {
    Page<CommentDTO> getParentComments(Integer postId);

    Page<CommentDTO> getCommentReplies(Integer commentId);

    CommentDTO createComment(CommentDTO commentDTO);
}
