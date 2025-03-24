package personal.social.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import personal.social.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getParentComments(Long postId, Integer pageNumber, Integer pageSize);

    Page<CommentDTO> getCommentReplies(Long commentId, Pageable pageable);

    CommentDTO createComment(CommentDTO commentDTO);
}
