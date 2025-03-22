package personal.social.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import personal.social.model.Post;
import personal.social.model.PostContent;
import personal.social.model.User;

public interface PostService {
    PostContent createPost(PostContent postContent, User user);

    Page<Post> getAllPosts(Pageable pageable);

    Post getPostById(Integer postId);

    Post updatePost(Post post);

    void deletePost(Integer postId);


}
