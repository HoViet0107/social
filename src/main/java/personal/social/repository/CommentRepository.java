package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import personal.social.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "CALL GetTopLevelComments(:postId, :pageNumber, :pageSize)", nativeQuery = true)
    List<Object[]> findTopLevelComments(
            @Param("postId") Long postId,
            @Param("pageNumber") Integer pageNumber,
            @Param("pageSize") Integer pageSize);
}
