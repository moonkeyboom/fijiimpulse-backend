package sa3.fijiimpulse.repositories;

import sa3.fijiimpulse.entity.User;

import java.util.List;

public interface UserDAO {
    void save(User user);
    void delete(Integer userId);
    User getUserById(Integer userId);
    List<User> getAllUsers();
    void updateUser(User user);
}
