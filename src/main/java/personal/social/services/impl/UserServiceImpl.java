package personal.social.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import personal.social.config.JwtUtil;
import personal.social.dto.AuthResponse;
import personal.social.dto.UserDTO;
import personal.social.enums.Role;
import personal.social.model.User;
import personal.social.repository.UserRepository;
import personal.social.services.UserService;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepo, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse login(UserDTO userDTO) {
        User existedUser = userRepo.findByEmail(userDTO.getEmail());
        if(existedUser == null) {
            throw new RuntimeException("User not found!");
        }
        if(!passwordEncoder.matches(userDTO.getPassword(), existedUser.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        // generate token
        String token = jwtUtil.generateToken(userDTO.getEmail());
        AuthResponse authResponse = new AuthResponse(token, "Login successfully!");
        return authResponse;
    }

    @Override
    public User register(User user) {
        if(userRepo.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already existed!");
        }

        if (userRepo.findByPhone(user.getPhone()) != null) {
            throw new RuntimeException("Phone number already existed!");
        }

        if(!validatePassword(user)) {
            throw new RuntimeException("Invalid password!");
        }
        // encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated_at(LocalDateTime.now());
        user.setRole(Role.USER);
        // save user
        return userRepo.save(user);
    }

    private boolean validatePassword(User user) {
        // validate password
        /*
        * 1. at least 8 characters
        * 2. at least 1 number
        * 3. at least 1 special character
        * 4. at least 1 uppercase
        * 5. at least 1 lowercase
        * */
        if (user.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters!");
        }
        if (user.getPassword().length() > 16) {
            throw new RuntimeException("Password cannot be longer than 16 characters!");
        }
        if(!user.getPassword().matches(".*[0-9].*")) {
            throw new RuntimeException("Password must contain at least 1 number!");
        }
        if(!user.getPassword().matches(".*[A-Z].*")) {
            throw new RuntimeException("Password must contain at least 1 uppercase character!");
        }
        if(!user.getPassword().matches(".*[a-z].*")) {
            throw new RuntimeException("Password must contain at least 1 lowercase character!");
        }
        if(!user.getPassword().matches(".*[!@#$%^&*()].*")) {
            throw new RuntimeException("Password must contain at least 1 special character!");
        }
        return true;
    }
}
