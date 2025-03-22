package personal.social.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import personal.social.model.Post;

public interface PostService {
    Post createPost(Post post);

    Page<Post> getAllPosts(Pageable pageable);

    Post getPostById(Integer postId);

    Post updatePost(Post post);

    void deletePost(Integer postId);


}
