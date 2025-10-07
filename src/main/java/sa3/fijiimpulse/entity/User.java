package sa3.fijiimpulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String email;
    private String username;
    private String role;
    private String passwordHash;
    private java.sql.Timestamp createdAt;
}
