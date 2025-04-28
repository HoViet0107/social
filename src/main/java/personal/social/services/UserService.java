package personal.social.services;

import personal.social.dto.AuthResponse;
import personal.social.dto.UserDTO;
import personal.social.model.User;

import java.util.Optional;

public interface UserService {
    User register(User user);

    AuthResponse login(UserDTO userDTO);

    Optional<User> getUserById(Long userId);

    User getUserByEmail(User user);

    User editUser(User existedUser, User user);
}
