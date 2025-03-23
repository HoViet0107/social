package personal.social.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import personal.social.dto.PostDTO;
import personal.social.model.Post;
import personal.social.model.PostContent;
import personal.social.model.User;

public interface PostService {
    PostContent createPost(PostContent postContent, User user);

    Page<PostDTO> getAllPosts(Pageable pageable);

    Post getPostById(Integer postId);

    PostContent updatePost(PostContent postContent, User user);

    void deletePost(Integer postId);


}
