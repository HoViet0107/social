package personal.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.social.enums.Status;
import personal.social.model.Post;
import personal.social.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAll(Pageable pageable);

    Page<Post> findByUser(User user, Pageable pageable);
    Post findByPostId(Integer postId);

    Page<Post> findByUserAndStatus(User user, Status status, Pageable pageable);
}
