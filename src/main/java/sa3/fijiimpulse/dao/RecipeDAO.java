package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Recipe;

import java.util.List;

@Repository
public class RecipeDAO {
    private final JdbcTemplate jdbcTemplate;

    public RecipeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Recipe> findAll() {
        String sql = "SELECT * FROM RECIPE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Recipe(
                rs.getInt("Recipe_id")
        ));
    }

    public Recipe findById(int id) {
        String sql = "SELECT * FROM RECIPE WHERE Recipe_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Recipe(
                rs.getInt("Recipe_id")
        ));
    }

    public int insert(Recipe r) {
        String sql = "INSERT INTO RECIPE () VALUES ()"; // ไม่มี field นอกจาก PK auto_increment
        return jdbcTemplate.update(sql);
    }

    public int delete(int id) {
        String sql = "DELETE FROM RECIPE WHERE Recipe_id=?";
        return jdbcTemplate.update(sql, id);
    }
}