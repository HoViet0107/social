package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.social.model.CommentContent;

@Repository
public interface CommentContentRepository extends JpaRepository<CommentContent, Integer> {
}
