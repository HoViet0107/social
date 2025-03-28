package personal.social.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private final PagedResourcesAssembler<CommentDTO> pagedResourcesAssembler;

    @Autowired
    public CommentController(
            CommentService commentService,
            JwtUtil jwtUtil,
            UserRepository userRepos,
            PagedResourcesAssembler<CommentDTO> pagedResourcesAssembler){
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
        this.userRepos = userRepos;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping
//    @Transactional
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
        // get user by email
        User user = userRepos.findByEmail(jwtUtil.extractEmail(token));
        try{
            return ResponseEntity.ok(commentService.createComment(commentDTO,user));
        }catch(Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostParentComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize){
        try{
            return ResponseEntity.ok(commentService.getParentComments(postId, pageNumber, pageSize));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reply/{parentCommentId}")
    public ResponseEntity<?> getCommentReplies(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @PathVariable Long parentCommentId){
        try{
            return ResponseEntity.ok(commentService.getCommentReplies(postId, pageNumber, pageSize,parentCommentId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> editComment(
            @RequestBody CommentDTO commentDTO,
            HttpServletRequest request
    ){
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
        // get user by email
        User user = userRepos.findByEmail(jwtUtil.extractEmail(token));
        try{
            return ResponseEntity.ok(commentService.editComment(commentDTO, user));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("An error occur: "+ e.getMessage());
        }
    }

    @PutMapping("/{commentId}/delete")
    public ResponseEntity<?> deleteComment(
            @RequestBody CommentDTO commentDTO,
            HttpServletRequest request
    ){
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
        // get user by email
        User user = userRepos.findByEmail(jwtUtil.extractEmail(token));
        try{
            return ResponseEntity.ok(commentService.deleteComment(commentDTO, user));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("An error occur: "+ e.getMessage());
        }
    }
}
