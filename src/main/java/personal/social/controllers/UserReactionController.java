package personal.social.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.social.dto.ReactionRequest;
import personal.social.helper.CommonHelpers;
import personal.social.model.User;
import personal.social.services.UserReactionService;

@RestController
@RequestMapping("/api/user-reaction")
public class UserReactionController {
    private final UserReactionService userReactionService;
    private final CommonHelpers commonHelpers;

    @Autowired
    public UserReactionController(UserReactionService userReactionService, CommonHelpers commonHelpers) {
        this.userReactionService = userReactionService;
        this.commonHelpers = commonHelpers;
    }

    @Operation(summary = "User reaction to post and comment", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("")
    public ResponseEntity<?> userReaction(
            @RequestBody ReactionRequest reactionRequest,
            HttpServletRequest request){
        // extract existed user from token
        User user = commonHelpers.extractToken(request);
        try{
            return ResponseEntity.ok(userReactionService.userReaction(reactionRequest, user));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
