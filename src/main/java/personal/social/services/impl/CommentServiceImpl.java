package personal.social.services.impl;

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
    public List<CommentDTO> getParentComments(Long postId, Integer pageNumber, Integer pageSize) {
        // throw exception if post not found
        if(postRepos.findByPostId(postId) == null){
            throw new RuntimeException("Post not found!");
        }
        List<Object[]> results = commentRepos.findTopLevelComments(postId, pageNumber, pageSize);
        // throw exception if post have no comment
        if(results.isEmpty()){
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
        if(commentRepos.findById(parentCmtId) == null){
            throw new RuntimeException("Service: Comment not found!");
        }

        List<Object[]> results = commentRepos.findCommentReplies(postId, pageNumber, pageSize, parentCmtId);

        // throw exception if post have no comment
        if(results.isEmpty()){
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
        Optional<Comment> parentComment = commentRepos.findById(commentDTO.getParentCommentId());
        newComment.setCommentStatus(Status.ACTIVE);
        newComment.setCreatedAt(currentTime);
        newComment.setLastUpdated(currentTime);
        newComment.setPost(postRepos.findByPostId(commentDTO.getPostId()));
        newComment.setUser(user);
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
