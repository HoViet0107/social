package personal.social.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import personal.social.dto.PostDTO;
import personal.social.model.Post;
import personal.social.model.PostContent;
import personal.social.model.User;

import java.util.Optional;

public interface PostService {
    PostContent createPost(PostContent postContent, User user);

    Page<PostDTO> getAllPosts(Pageable pageable);

    Optional<Post> getPostById(Long postId);

    PostContent updatePost(PostContent postContent, User user);

    void updatePostStatus(Long postId, User user);


}
