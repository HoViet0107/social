package personal.social.services;
import personal.social.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getParentComments(Long postId, Integer pageNumber, Integer pageSize);

    List<CommentDTO> getCommentReplies(Long postId, Integer pageNumber, Integer pageSize, Long parentCmtId);

    CommentDTO createComment(CommentDTO commentDTO);
}
