package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Material;

import java.util.List;

@Repository
public class MaterialDAO {
    private final JdbcTemplate jdbcTemplate;

    public MaterialDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Material> findAll() {
        String sql = "SELECT * FROM MATERIAL";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Material(
                rs.getInt("Material_id"),
                rs.getInt("Supplier_id"),
                rs.getString("Material_name"),
                rs.getInt("Stock_quantity"),
                rs.getString("Material_image"),
                rs.getTimestamp("Last_updated")
        ));
    }

    public Material findById(int id) {
        String sql = "SELECT * FROM MATERIAL WHERE Material_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Material(
                rs.getInt("Material_id"),
                rs.getInt("Supplier_id"),
                rs.getString("Material_name"),
                rs.getInt("Stock_quantity"),
                rs.getString("Material_image"),
                rs.getTimestamp("Last_updated")
        ));
    }

    public int insert(Material m) {
        String sql = "INSERT INTO MATERIAL (Supplier_id, Material_name, Stock_quantity, Material_image) VALUES (?,?,?,?)";
        return jdbcTemplate.update(sql, m.getSupplierId(), m.getMaterialName(), m.getStockQuantity(), m.getMaterialImage());
    }

    public int update(Material m) {
        String sql = "UPDATE MATERIAL SET Supplier_id=?, Material_name=?, Stock_quantity=?, Material_image=? WHERE Material_id=?";
        return jdbcTemplate.update(sql, m.getSupplierId(), m.getMaterialName(), m.getStockQuantity(), m.getMaterialImage(), m.getMaterialId());
    }

    public int delete(int id) {
        String sql = "DELETE FROM MATERIAL WHERE Material_id=?";
        return jdbcTemplate.update(sql, id);
    }

    // Use Case 4M: เพิ่มจำนวน Stock
    public int addMaterialStock(int materialId, int additionalQuantity) {
        String sql = "UPDATE MATERIAL SET Stock_quantity = Stock_quantity + ?, Last_updated = CURRENT_TIMESTAMP WHERE Material_id=?";
        return jdbcTemplate.update(sql, additionalQuantity, materialId);
    }

    // Use Case 4M: ลด Stock
    public int reduceMaterialStock(int materialId, int reduceQuantity) {
        String sql = "UPDATE MATERIAL SET Stock_quantity = Stock_quantity - ?, Last_updated = CURRENT_TIMESTAMP WHERE Material_id=? AND Stock_quantity >= ?";
        return jdbcTemplate.update(sql, reduceQuantity, materialId, reduceQuantity);
    }
}