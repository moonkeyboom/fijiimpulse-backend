package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.User;

import java.util.List;

@Repository
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("User_id"),
                rs.getString("Email"),
                rs.getString("Username"),
                rs.getString("Role"),
                rs.getString("Password_hash"),
                rs.getTimestamp("Created_at")
        ));
    }

    public User findById(int id) {
        String sql = "SELECT * FROM USERS WHERE User_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new User(
                rs.getInt("User_id"),
                rs.getString("Email"),
                rs.getString("Username"),
                rs.getString("Role"),
                rs.getString("Password_hash"),
                rs.getTimestamp("Created_at")
        ));
    }

    public int insert(User u) {
        String sql = "INSERT INTO USERS (Email, Username, Role, Password_hash, Created_at) VALUES (?,?,?,?,?)";
        return jdbcTemplate.update(sql, u.getEmail(), u.getUsername(), u.getRole(), u.getPasswordHash(), u.getCreatedAt());
    }

    public int update(User u) {
        String sql = "UPDATE USERS SET Email=?, Username=?, Role=?, Password_hash=? WHERE User_id=?";
        return jdbcTemplate.update(sql, u.getEmail(), u.getUsername(), u.getRole(), u.getPasswordHash(), u.getUserId());
    }

    public int delete(int id) {
        String sql = "DELETE FROM USERS WHERE User_id=?";
        return jdbcTemplate.update(sql, id);
    }
}