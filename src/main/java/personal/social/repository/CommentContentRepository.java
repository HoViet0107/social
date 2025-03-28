package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.social.model.CommentContent;

import java.time.LocalDateTime;

@Repository
public interface CommentContentRepository extends JpaRepository<CommentContent, Long> {
    CommentContent findByCommentCommentIdAndLastUpdated(Long commentId, LocalDateTime lastUpdated);
}
