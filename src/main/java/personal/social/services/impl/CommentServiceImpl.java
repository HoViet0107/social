package personal.social.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.social.dto.CommentDTO;
import personal.social.enums.Status;
import personal.social.model.Comment;
import personal.social.model.CommentContent;
import personal.social.repository.CommentContentRepository;
import personal.social.repository.CommentRepository;
import personal.social.repository.PostRepository;
import personal.social.repository.UserRepository;
import personal.social.services.CommentService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepos;
    private final UserRepository userRepos;
    private final PostRepository postRepos;
    private final CommentContentRepository cmtContentRepos;

    @Autowired
    public CommentServiceImpl (
            CommentRepository commentRepos,
            UserRepository userRepos,
            PostRepository postRepos,
            CommentContentRepository cmtContentRepos) {
        this.commentRepos = commentRepos;
        this.userRepos = userRepos;
        this.postRepos = postRepos;
        this.cmtContentRepos = cmtContentRepos;
    }

    @Override
    public List<CommentDTO> getParentComments(Long postId, int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Page<CommentDTO> getCommentReplies(Long commentId, Pageable pageable) {
        return null;
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        LocalDateTime currentTime = LocalDateTime.now();
        // create comment obj
        Comment newComment = new Comment();
        // get parent comment
        Optional<Comment> parentComment = commentRepos.findById(commentDTO.getParentCommentId());
        newComment.setCommentStatus(Status.ACTIVE);
        newComment.setCreatedAt(currentTime);
        newComment.setLastUpdated(currentTime);
        newComment.setPost(postRepos.findByPostId(commentDTO.getPostId()));
        newComment.setUser(userRepos.findByUserId(commentDTO.getUserId()));
        newComment.setParentComment(parentComment.orElse(null));
        try{
            commentRepos.save(newComment); // save to database
        }catch (Exception e){
            throw new RuntimeException("Faile to create comment: "+e.getMessage());
        }

        // create content for comment
        CommentContent cmtContent = new CommentContent();
        cmtContent.setComment(newComment);
        cmtContent.setContent(commentDTO.getContent());
        cmtContent.setLastUpdated(currentTime);
        try{
            cmtContentRepos.save(cmtContent); // save to database
        }catch (Exception e){
            throw new RuntimeException("Failed to create comment: " + e.getMessage());
        }
        // set time and status to commentDTO
        commentDTO.setCommentId(newComment.getCommentId());
        commentDTO.setCommentStatus(newComment.getCommentStatus());
        commentDTO.setCreatedAt(newComment.getCreatedAt());
        commentDTO.setLikes(newComment.getLikes());
        commentDTO.setDislikes(newComment.getDislikes());

        return commentDTO;
    }
}
