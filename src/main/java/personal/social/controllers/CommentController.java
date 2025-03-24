package personal.social.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.social.config.JwtUtil;
import personal.social.dto.CommentDTO;
import personal.social.model.User;
import personal.social.repository.UserRepository;
import personal.social.services.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepos;

    @Autowired
    public CommentController(
            CommentService commentService,
            JwtUtil jwtUtil,
            UserRepository userRepos){
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
        this.userRepos = userRepos;
    }

    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestBody CommentDTO commentDTO,
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

        try{
            return ResponseEntity.ok(commentService.createComment(commentDTO));
        }catch(Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
