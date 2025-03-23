package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.social.model.Post;
import personal.social.model.PostContent;

@Repository
public interface PostContentRepository extends JpaRepository<PostContent, Integer> {
}
