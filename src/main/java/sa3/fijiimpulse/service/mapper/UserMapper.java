package sa3.fijiimpulse.service.mapper;

import org.springframework.jdbc.core.RowMapper;
import sa3.fijiimpulse.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("role"),
                rs.getString("password_hash"),
                rs.getTimestamp("created_at")
        );
    }
}
