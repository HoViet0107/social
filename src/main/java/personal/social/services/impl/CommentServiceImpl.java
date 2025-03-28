package personal.social.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.social.dto.CommentDTO;
import personal.social.enums.Status;
import personal.social.model.Comment;
import personal.social.model.CommentContent;
import personal.social.model.User;
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
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepos;
    private final UserRepository userRepos;
    private final PostRepository postRepos;
    private final CommentContentRepository cmtContentRepos;

    @Autowired
    public CommentServiceImpl(
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
    public List<CommentDTO> getParentComments(Long postId, Integer pageNumber, Integer pageSize) {
        // throw exception if post not found
        if (postRepos.findByPostId(postId) == null) {
            throw new RuntimeException("Post not found!");
        }
        List<Object[]> results = commentRepos.findTopLevelComments(postId, pageNumber, pageSize);
        // throw exception if post have no comment
        if (results.isEmpty()) {
            throw new RuntimeException("Post have no comment!");
        }
        return results.stream().map(obj -> new CommentDTO(
                ((Number) obj[0]).longValue(),  // commentId
                ((Timestamp) obj[1]).toLocalDateTime(), // createdAt
                ((Timestamp) obj[2]).toLocalDateTime(), // lastUpdated
                Status.valueOf((String) obj[3]),    // commentStatus
                ((Number) obj[4]).longValue(),   // likes
                ((Number) obj[5]).longValue(),   // dislikes
                (String) obj[6],    // content
                ((Number) obj[7]).longValue(),   // userId
                ((Number) obj[8]).longValue(),   // postId
                ((Number) obj[9]).longValue()    // parentCommentId
        )).collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentReplies(Long postId, Integer pageNumber, Integer pageSize, Long parentCmtId) {
        // throw exception if parent comment not found
        if (commentRepos.findById(parentCmtId) == null) {
            throw new RuntimeException("Service: Comment not found!");
        }

        List<Object[]> results = commentRepos.findCommentReplies(postId, pageNumber, pageSize, parentCmtId);

        // throw exception if post have no comment
        if (results.isEmpty()) {
            throw new RuntimeException("Service: Comment have no replies!");
        }

        return results.stream().map(obj -> new CommentDTO(
                ((Number) obj[0]).longValue(),  // commentId
                ((Timestamp) obj[1]).toLocalDateTime(), // createdAt
                ((Timestamp) obj[2]).toLocalDateTime(), // lastUpdated
                Status.valueOf((String) obj[3]),    // commentStatus
                ((Number) obj[4]).longValue(),   // likes
                ((Number) obj[5]).longValue(),   // dislikes
                (String) obj[6],    // content
                ((Number) obj[7]).longValue(),   // userId
                ((Number) obj[8]).longValue(),   // postId
                ((Number) obj[9]).longValue()    // parentCommentId
        )).collect(Collectors.toList());
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO, User user) {
        LocalDateTime currentTime = LocalDateTime.now();
        // create comment obj
        Comment newComment = new Comment();
        // get parent comment
        Comment parentComment = new Comment();
        if(commentDTO.getParentCommentId() != null){
            parentComment = commentRepos.findById(commentDTO.getParentCommentId()).orElse(null);
        }
        newComment.setCommentStatus(Status.ACTIVE);
        newComment.setCreatedAt(currentTime);
        newComment.setLastUpdated(currentTime);
        newComment.setPost(postRepos.findByPostId(commentDTO.getPostId()));
        newComment.setUser(user);
        newComment.setParentComment(parentComment);

        Comment savedCmt = commentRepos.save(newComment); // save to database
        System.out.println(savedCmt);
        // create content for comment
        CommentContent cmtContent = new CommentContent();
        cmtContent.setComment(savedCmt);
        cmtContent.setContent(commentDTO.getContent());
        cmtContent.setLastUpdated(currentTime);
        cmtContentRepos.save(cmtContent); // save to database

        // set time and status to commentDTO
        commentDTO.setCommentId(savedCmt.getCommentId());
        commentDTO.setCommentStatus(savedCmt.getCommentStatus());
        commentDTO.setCreatedAt(savedCmt.getCreatedAt());
        commentDTO.setLikes(savedCmt.getLikes());
        commentDTO.setDislikes(savedCmt.getDislikes());

        return commentDTO;
    }

    @Override
    public CommentDTO editComment(CommentDTO commentDTO, User user) {
        // Get existed comment
        Comment existedComment = commentRepos.findByCommentId(commentDTO.getCommentId());

        // Check if comment exists
        if (existedComment == null) {
            throw new RuntimeException("Comment not found with ID: " + commentDTO.getCommentId());
        }

        // Check if user has permission
        if (!existedComment.getUser().equals(user)) {
            throw new RuntimeException(String.format("User %s doesn't have permission to edit this comment", user.getUserId()));
        }

        LocalDateTime currentTime = LocalDateTime.now();

        // Get existing comment content
        CommentContent existedCmtContent = cmtContentRepos.findByCommentCommentIdAndLastUpdated(
                commentDTO.getCommentId(), existedComment.getLastUpdated()
        );

        boolean hasChanges = false;

        // Check for content changes
        boolean contentChanged = !existedCmtContent.getContent().equals(commentDTO.getContent());

        // Check for likes changes
        if (commentDTO.getLikes() != null &&
                !existedComment.getLikes().equals(commentDTO.getLikes())) {
            existedComment.setLikes(commentDTO.getLikes());
            hasChanges = true;
        }

        // Check for dislikes changes
        if (commentDTO.getDislikes() != null &&
                !existedComment.getDislikes().equals(commentDTO.getDislikes())) {
            existedComment.setDislikes(commentDTO.getDislikes());
            hasChanges = true;
        }

        // If content changed or other properties changed
        if (contentChanged || hasChanges) {
            // Update timestamp
            existedComment.setLastUpdated(currentTime);

            // Save comment changes
            commentRepos.save(existedComment);

            // If content changed, create new content record
            if (contentChanged) {
                CommentContent newCmtContent = new CommentContent(commentDTO.getContent(), currentTime, existedComment);
                cmtContentRepos.save(newCmtContent);
            }

            // Create response DTO
            CommentDTO updatedDto = new CommentDTO(
                    existedComment.getCommentId(),
                    existedComment.getCreatedAt(),
                    existedComment.getLastUpdated(),
                    existedComment.getCommentStatus(),
                    existedComment.getLikes(),
                    existedComment.getDislikes(),
                    contentChanged ? commentDTO.getContent() : existedCmtContent.getContent(),
                    existedComment.getUser().getUserId(),
                    existedComment.getPost().getPostId(),
                    existedComment.getParentComment() != null ? existedComment.getParentComment().getCommentId() : null
            );
            return updatedDto;
        }

        // If no changes, return the original DTO with current values
        return new CommentDTO(
                existedComment.getCommentId(),
                existedComment.getCreatedAt(),
                existedComment.getLastUpdated(),
                existedComment.getCommentStatus(),
                existedComment.getLikes(),
                existedComment.getDislikes(),
                existedCmtContent.getContent(),
                existedComment.getUser().getUserId(),
                existedComment.getPost().getPostId(),
                existedComment.getParentComment() != null ? existedComment.getParentComment().getCommentId() : null
        );
    }

    @Override
    public String deleteComment(CommentDTO commentDTO, User user) {
        // Get existed comment
        Comment existedComment = commentRepos.findByCommentId(commentDTO.getCommentId());

        // Check if comment exists
        if (existedComment == null) {
            throw new RuntimeException("Comment not found with ID: " + commentDTO.getCommentId());
        }

        // Check if user has permission
        if (!existedComment.getUser().equals(user)) {
            throw new RuntimeException(String.format("User %s doesn't have permission to edit this comment", user.getUserId()));
        }

        if (existedComment.getCommentStatus().equals(Status.ACTIVE)){
            existedComment.setCommentStatus(Status.INACTIVE);
            commentRepos.save(existedComment);
        }else {
            existedComment.setCommentStatus(Status.ACTIVE);
            commentRepos.save(existedComment);
            return "Recover comment success!";
        }
        return "Comment deleted!";
    }
}
