package sa3.fijiimpulse.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Recipe;

import java.util.List;

@Repository
public class RecipeRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecipeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Recipe r) {
        String sql = "INSERT INTO recipes (model_id) VALUES (?)";
        return jdbcTemplate.update(sql, r.getModelId());
    }

    public int update(Recipe r) {
        String sql = "UPDATE recipes SET model_id=? WHERE recipe_id=?";
        return jdbcTemplate.update(sql, r.getModelId(), r.getRecipeId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM recipes WHERE recipe_id=?";
        return jdbcTemplate.update(sql, id);
    }

    public Recipe findById(Long id) {
        String sql = "SELECT * FROM recipes WHERE recipe_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Recipe(rs.getLong("recipe_id"), rs.getLong("model_id")), id);
    }

    public List<Recipe> findAll() {
        String sql = "SELECT * FROM recipes";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Recipe(rs.getLong("recipe_id"), rs.getLong("model_id")));
    }
}