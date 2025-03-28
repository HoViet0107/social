package personal.social.services;
import personal.social.dto.CommentDTO;
import personal.social.model.User;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getParentComments(Long postId, Integer pageNumber, Integer pageSize);

    List<CommentDTO> getCommentReplies(Long postId, Integer pageNumber, Integer pageSize, Long parentCmtId);

    CommentDTO createComment(CommentDTO commentDTO, User user);

    CommentDTO editComment(CommentDTO commentDTO, User user);

    String deleteComment(CommentDTO commentDTO, User user);
}
