package personal.social.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.social.enums.Status;
import personal.social.model.Post;
import personal.social.model.PostContent;
import personal.social.model.User;
import personal.social.repository.PostContentRepository;
import personal.social.repository.PostRepository;
import personal.social.services.PostService;

import java.time.LocalDateTime;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepos;
    private final PostContentRepository pcRepos;
    @Autowired
    public PostServiceImpl(
            PostRepository postRepos,
            PostContentRepository pcRepos) {
        this.postRepos = postRepos;
        this.pcRepos = pcRepos;
    }


    @Override
    public PostContent createPost(PostContent postContent, User user) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        // create post
        Post post = new Post();
        try{
            post.setCreatedAt(currentDateTime);
            post.setLastUpdated(currentDateTime);
            post.setStatus(Status.ACTIVE);
            post.setUser(user);
            postRepos.save(post) ;
        } catch (Exception e){
            throw new RuntimeException("Create Post failed!");
        }
        // save post content
        try{
            postContent.setPost(post);
            postContent.setUpdatedAt(currentDateTime);
        } catch (Exception e){
            throw new RuntimeException("Save Post content failed!");
        }
        return pcRepos.save(postContent);
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
