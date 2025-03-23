package personal.social.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.social.dto.PostDTO;
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
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        return postRepos.findAllPosts(pageable);
    }

    @Override
    public Post getPostById(Integer postId) {
        return null;
    }

    @Override
    public PostContent updatePost(PostContent postContent, User user) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        // get existed post by post id
        Post existedPost = postRepos.findByPostId(postContent.getPost().getPostId());
        // set new update time
        existedPost.setLastUpdated(currentDateTime);
        postRepos.save(existedPost);
        // save new post content for existedPost and return it
        PostContent newContent = new PostContent();
        newContent.setContent(postContent.getContent());
        newContent.setUpdatedAt(currentDateTime);
        newContent.setPost(existedPost);
        return pcRepos.save(newContent);
    }

    @Override
    public void deletePost(Integer postId) {

    }
}
