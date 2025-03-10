package personal.social.services;

import personal.social.dto.AuthResponse;
import personal.social.dto.UserDTO;
import personal.social.model.User;

public interface UserService {
    User register(User user);
    AuthResponse login(UserDTO userDTO);
}
