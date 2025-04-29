package personal.social.helper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import personal.social.config.JwtUtil;
import personal.social.model.User;
import personal.social.repository.UserRepository;

@Component
public class CommonHelpers {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepos;

    @Autowired
    public CommonHelpers(JwtUtil jwtUtil, UserRepository userRepos){
        this.jwtUtil = jwtUtil;
        this.userRepos = userRepos;
    }

    public User extractToken(HttpServletRequest request){
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
