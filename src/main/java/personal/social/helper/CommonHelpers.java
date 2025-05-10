package personal.social.helper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import personal.social.config.JwtUtil;
import personal.social.model.Users;
import personal.social.repository.UserRepository;

/**
 * Utility class providing common helper methods used across the application
 * Contains methods for token extraction, user authentication, and entity operations
 */
@Component
@Slf4j
public class CommonHelpers {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepos;

    @Autowired
    public CommonHelpers(JwtUtil jwtUtil, UserRepository userRepos){
        this.jwtUtil = jwtUtil;
        this.userRepos = userRepos;
    }

    /**
     * Extracts and validates JWT token from HTTP request header
     * Retrieves the associated user from the database
     *
     * @param request The HTTP request containing the Authorization header
     * @return The authenticated user
     * @throws RuntimeException if token is missing, invalid, expired, or user not found
     */
    public Users extractToken(HttpServletRequest request){
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

        Users existedUser = userRepos.findByEmail(jwtUtil.extractEmail(token));
        if(existedUser == null){
            throw new RuntimeException("User not existed!");
        }

        return existedUser;
    }

    /**
     * Helper method to save an entity with consistent error handling.
     * This can be reused across different methods.
     *
     * @param <T> The entity type
     * @param entity The entity to save
     * @param repository The JPA repository for the entity
     * @param entityDescription A description of the entity for error messages
     * @throws RuntimeException if saving fails
     */
    public <T> void saveEntity(T entity, JpaRepository<T, ?> repository, String entityDescription) {
        try {
            repository.save(entity);
        } catch (Exception e) {
            String errorMessage = String.format("Failed to save %s: %s", entityDescription, e.getMessage());
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
