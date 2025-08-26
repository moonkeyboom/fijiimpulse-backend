package sa3.fijiimpulse.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.RecipeMaterial;

import java.util.List;

@Repository
public class RecipeMaterialRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecipeMaterialRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public int save(RecipeMaterial rm) {
        String sql = "INSERT INTO recipe_materials (recipe_id, material_id, quantity) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, rm.getRecipeId(), rm.getMaterialId(), rm.getQuantity());
    }

    public int update(RecipeMaterial rm) {
        String sql = "UPDATE recipe_materials SET quantity=? WHERE recipe_id=? AND material_id=?";
        return jdbcTemplate.update(sql, rm.getQuantity(), rm.getRecipeId(), rm.getMaterialId());
    }

    public int delete(Long recipeId, Long materialId) {
        String sql = "DELETE FROM recipe_materials WHERE recipe_id=? AND material_id=?";
        return jdbcTemplate.update(sql, recipeId, materialId);
    }

    public RecipeMaterial findById(Long recipeId, Long materialId) {
        String sql = "SELECT * FROM recipe_materials WHERE recipe_id=? AND material_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        new RecipeMaterial(rs.getLong("recipe_id"),
                                rs.getLong("material_id"),
                                rs.getBigDecimal("quantity")),
                recipeId, materialId);
    }

    public List<RecipeMaterial> findAll() {
        String sql = "SELECT * FROM recipe_materials";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RecipeMaterial(rs.getLong("recipe_id"),
                        rs.getLong("material_id"),
                        rs.getBigDecimal("quantity")));
    }
}