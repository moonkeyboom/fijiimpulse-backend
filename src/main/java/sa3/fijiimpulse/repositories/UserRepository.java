package sa3.fijiimpulse.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.User;

import java.util.List;


@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(User user) {
        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
    }

    public int update(User user) {
        String sql = "UPDATE users SET username=?, email=?, password=?, role=? WHERE user_id=?";
        return jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getUserId());
    }

    public int delete(Long userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        return jdbcTemplate.update(sql, userId);
    }

    public User findById(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                rs.getLong("user_id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role")
        ), userId);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("user_id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role")
        ));
    }
}