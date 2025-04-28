package personal.social.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.social.config.JwtUtil;
import personal.social.model.User;
import personal.social.repository.UserRepository;
import personal.social.services.UserService;

import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepos;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, UserRepository userRepos){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepos = userRepos;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        try{
            return ResponseEntity.ok(userService.getUserById(userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("user-details")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request){
        try{
            User existedUser = extractToken(request);
            return ResponseEntity.ok(userService.getUserByEmail(existedUser));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> editUser(
            @PathVariable Long userId,
            @RequestBody User user,
            HttpServletRequest request
    ){
        User existedUser = extractToken(request);
        if(existedUser == null){
            throw new RuntimeException("User not existed!");
        }

        if(!Objects.equals(existedUser.getUserId(), userId)){
            return ResponseEntity.status(403).body("You don't have permission to edit this user");
        }

        try{
            return ResponseEntity.ok(userService.editUser(existedUser, user));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
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
