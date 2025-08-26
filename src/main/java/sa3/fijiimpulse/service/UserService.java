package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.entity.User;
import sa3.fijiimpulse.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int createUser(User user) {
        return userRepository.save(user);
    }

    public int updateUser(User user) {
        return userRepository.update(user);
    }

    public int deleteUser(Long userId) {
        return userRepository.delete(userId);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
