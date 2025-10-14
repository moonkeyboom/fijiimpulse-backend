package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.RecipeMaterial;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    // Use Case 3M: ดึงวัสดุที่ต้องใช้สำหรับ Recipe นี้
    public List<RecipeMaterial> findByRecipeId(int recipeId) {
        String sql = "SELECT * FROM RECIPE_MATERIAL WHERE Recipe_id=?";
        return jdbcTemplate.query(sql, new Object[]{recipeId}, (rs, rowNum) -> new RecipeMaterial(
                rs.getInt("Recipe_id"),
                rs.getInt("Material_id"),
                rs.getInt("Required_quantity")
        ));
    }

    public Map<Integer, Map<String, Object>> findByModelId(int modelId) {
        String sql = """
        SELECT 
            m.material_id,
            m.material_name, 
            m.material_image, 
            rm.required_quantity 
        FROM PRODUCT_MODEL pm 
        JOIN RECIPE r ON pm.recipe_id = r.recipe_id 
        JOIN RECIPE_MATERIAL rm ON r.recipe_id = rm.recipe_id 
        JOIN MATERIAL m ON rm.material_id = m.material_id 
        WHERE pm.model_id = ?
    """;

        return jdbcTemplate.query(sql, new Object[]{modelId}, rs -> {
            Map<Integer, Map<String, Object>> result = new LinkedHashMap<>();
            while (rs.next()) {
                Map<String, Object> materialDetail = new HashMap<>();
                materialDetail.put("material_name", rs.getString("material_name"));
                materialDetail.put("material_image", rs.getString("material_image"));
                materialDetail.put("required_quantity", rs.getInt("required_quantity"));
                result.put(rs.getInt("material_id"), materialDetail);
            }
            return result;
        });
    }
}
