package personal.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.social.dto.PostDTO;
import personal.social.enums.Status;
import personal.social.model.Post;
import personal.social.model.User;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT new personal.social.dto.PostDTO(p.postId, p.createdAt, p.lastUpdated, " +
            "p.status, pc.content, p.user.userId) " +
            "FROM Post p " +
            "JOIN PostContent pc ON p.lastUpdated = pc.updatedAt")
    Page<PostDTO> findAllPosts(Pageable pageable);

    Page<Post> findByUser(User user, Pageable pageable);
    Post findByPostId(Integer postId);

    @Query(value = "SELECT new personal.social.dto.PostDTO(p.postId, p.createdAt, p.lastUpdated, " +
            "p.status, pc.content, p.user.userId) " +
            "FROM Post p " +
            "JOIN PostContent pc ON p.lastUpdated = pc.updatedAt " +
            "WHERE p.postId = :postId")
    Optional<Post> findById(Integer postId);

    Page<Post> findByUserAndStatus(User user, Status status, Pageable pageable);
}
