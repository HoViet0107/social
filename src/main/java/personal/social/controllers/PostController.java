package personal.social.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.social.config.JwtUtil;
import personal.social.dto.PostDTO;
import personal.social.model.Post;
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
    private final UserRepository userRepo;
    private final PagedResourcesAssembler<PostDTO> pagedResourcesAssembler;

    @Autowired
    public PostController(
            PostService postService,
            JwtUtil jwtUtil,
            UserRepository userRepo,
            PagedResourcesAssembler<PostDTO> pagedResourcesAssembler) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<?> getAllPosts(Pageable pageable) {
        // use PagedModel for RESTful API
        Page<PostDTO> posts = postService.getAllPosts(pageable);
        return pagedResourcesAssembler.toModel(posts);
    }

    @Operation(summary = "Create new post", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody PostContent postContent,
            HttpServletRequest request) {
        // extract token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // get token without "Bearer "
        } else {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        if(jwtUtil.isTokenExpired(token)){
            return ResponseEntity.badRequest().body("Token expired!");
        }
        // extract email and get existed user by email
        User existedUser = userRepo.findByEmail(jwtUtil.extractEmail(token));

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
        // extract token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // get token without "Bearer "
        } else {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        if(jwtUtil.isTokenExpired(token)){
            return ResponseEntity.badRequest().body("Token expired!");
        } // end extract token
        // get user by email
        User user = userRepo.findByEmail(jwtUtil.extractEmail(token));
        try{
            // update post new content
            return ResponseEntity.ok(postService.updatePost(postContent, user));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
