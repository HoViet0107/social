package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.social.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
