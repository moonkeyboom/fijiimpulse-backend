package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.RecipeMaterial;

import java.util.List;

@Repository
public class RecipeMaterialDAO {
    private final JdbcTemplate jdbcTemplate;

    public RecipeMaterialDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RecipeMaterial> findAll() {
        String sql = "SELECT * FROM RECIPE_MATERIAL";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new RecipeMaterial(
                rs.getInt("Recipe_id"),
                rs.getInt("Material_id"),
                rs.getInt("Required_quantity")
        ));
    }

    public RecipeMaterial findById(int recipeId, int materialId) {
        String sql = "SELECT * FROM RECIPE_MATERIAL WHERE Recipe_id=? AND Material_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{recipeId, materialId}, (rs, rowNum) -> new RecipeMaterial(
                rs.getInt("Recipe_id"),
                rs.getInt("Material_id"),
                rs.getInt("Required_quantity")
        ));
    }

    public int insert(RecipeMaterial rm) {
        String sql = "INSERT INTO RECIPE_MATERIAL (Recipe_id, Material_id, Required_quantity) VALUES (?,?,?)";
        return jdbcTemplate.update(sql, rm.getRecipeId(), rm.getMaterialId(), rm.getRequiredQuantity());
    }

    public int update(RecipeMaterial rm) {
        String sql = "UPDATE RECIPE_MATERIAL SET Required_quantity=? WHERE Recipe_id=? AND Material_id=?";
        return jdbcTemplate.update(sql, rm.getRequiredQuantity(), rm.getRecipeId(), rm.getMaterialId());
    }

    public int delete(int recipeId, int materialId) {
        String sql = "DELETE FROM RECIPE_MATERIAL WHERE Recipe_id=? AND Material_id=?";
        return jdbcTemplate.update(sql, recipeId, materialId);
    }
}
