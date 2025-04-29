package personal.social.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.social.config.JwtUtil;
import personal.social.helper.CommonHelpers;
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
    private final CommonHelpers commonHelpers;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, UserRepository userRepos, CommonHelpers commonHelpers){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepos = userRepos;
        this.commonHelpers = commonHelpers;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        try{
            return ResponseEntity.ok(userService.getUserById(userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "get user profile", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("user-details")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request){
        try{
            User existedUser = commonHelpers.extractToken(request);
            return ResponseEntity.ok(userService.getUserByEmail(existedUser));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @Operation(summary = "edit user profile", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{userId}")
    public ResponseEntity<?> editUser(
            @PathVariable Long userId,
            @RequestBody User user,
            HttpServletRequest request
    ){
        User existedUser = commonHelpers.extractToken(request);
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
}
