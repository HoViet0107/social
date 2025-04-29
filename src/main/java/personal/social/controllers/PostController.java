package personal.social.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.social.config.JwtUtil;
import personal.social.dto.PostDTO;
import personal.social.model.PostContent;
import personal.social.model.User;
import personal.social.repository.UserRepository;
import personal.social.services.PostService;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepos;
    private final PagedResourcesAssembler<PostDTO> pagedResourcesAssembler;

    @Autowired
    public PostController(
            PostService postService,
            JwtUtil jwtUtil,
            UserRepository userRepos,
            PagedResourcesAssembler<PostDTO> pagedResourcesAssembler) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
        this.userRepos = userRepos;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<?> getAllPosts(Pageable pageable) {
        // use PagedModel for RESTful API
        Page<PostDTO> posts = postService.getAllPosts(pageable);
        return pagedResourcesAssembler.toModel(posts);
    }
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId){
        try{
            return ResponseEntity.ok(postService.getPostById(postId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Create new post", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody PostContent postContent,
            HttpServletRequest request) {
        // extract email and get existed user by email
        User existedUser = extractToken(request);

        // pass post content and user to createPost()
        try{
            return ResponseEntity.ok(postService.createPost(postContent, existedUser));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @Operation(summary = "Update post content", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping
    public ResponseEntity<?> updatePost(
        @RequestBody PostContent postContent,
        HttpServletRequest request){
        // extract existed user from token
        User user = extractToken(request);
        try{
            // update post new content
            return ResponseEntity.ok(postService.updatePost(postContent, user));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update status", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{postId}/status")
    public ResponseEntity<?> updatePostStatus(
            @PathVariable Long postId,
            HttpServletRequest request){
        // extract existed user from token
        User user = extractToken(request);
        try{
            postService.updatePostStatus(postId, user);
            return ResponseEntity.ok().body("Status changed!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    User extractToken(HttpServletRequest request){
        // extract token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // get token without "Bearer "
        } else {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        if(jwtUtil.isTokenExpired(token)){
            throw new RuntimeException("Token expired");
        } // end extract token

        User existedUser = userRepos.findByEmail(jwtUtil.extractEmail(token));
        if(existedUser == null){
            throw new RuntimeException("User not existed!");
        }

        return existedUser;
    }
}
