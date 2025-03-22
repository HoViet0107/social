package personal.social.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.social.model.Post;
import personal.social.repository.PostRepository;
import personal.social.services.PostService;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepos;
    @Autowired
    public PostServiceImpl(PostRepository postRepos) {
        this.postRepos = postRepos;
    }


    @Override
    public Post createPost(Post post) {
        return null;
    }

    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepos.findAll(pageable);
    }

    @Override
    public Post getPostById(Integer postId) {
        return null;
    }

    @Override
    public Post updatePost(Post post) {
        return null;
    }

    @Override
    public void deletePost(Integer postId) {

    }
}
