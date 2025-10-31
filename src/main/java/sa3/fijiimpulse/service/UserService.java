package sa3.fijiimpulse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.UserDAO;
import sa3.fijiimpulse.entity.User;

import java.sql.Timestamp;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean register(String username, String email, String password) {
        if (userDAO.findByEmail(email) != null) {
            return false;
        }
        String hash = passwordEncoder.encode(password);

        User user = new User(
                0,
                email,
                username,
                "USER",
                hash,
                new Timestamp(System.currentTimeMillis())
        );
        userDAO.insert(user);
        return true;
    }


    public boolean registerAdmin(String username, String email, String password) {
        if (userDAO.findByEmail(email) != null) {
            return false;
        }
        String hash  = passwordEncoder.encode(password);
        User user = new User(
                0,
                email,
                username,
                "ADMIN",
                hash,
                new Timestamp(System.currentTimeMillis())
        );
        userDAO.insert(user);
        return true;
    }

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) return null;

        boolean match  = passwordEncoder.matches(password, user.getPasswordHash());
        return match ? user : null;
    }
}
