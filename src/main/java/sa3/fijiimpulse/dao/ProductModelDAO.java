package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.ProductModel;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class ProductModelDAO {
    private final JdbcTemplate jdbcTemplate;

    public ProductModelDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductModel> findAll() {
        String sql = "SELECT * FROM PRODUCT_MODEL";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProductModel(
                rs.getInt("Model_id"),
                rs.getInt("Recipe_id"),
                rs.getString("Model_name"),
                rs.getBigDecimal("Price"),
                rs.getString("Model_image"),
                rs.getString("Description")
        ));
    }

    public ProductModel findById(int id) {
        String sql = "SELECT * FROM PRODUCT_MODEL WHERE Model_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new ProductModel(
                rs.getInt("Model_id"),
                rs.getInt("Recipe_id"),
                rs.getString("Model_name"),
                rs.getBigDecimal("Price"),
                rs.getString("Model_image"),
                rs.getString("Description")
        ));
    }

    public int insert(ProductModel pm) {
        String sql = "INSERT INTO PRODUCT_MODEL (Recipe_id, Model_name, Price, Model_image, Description) VALUES (?,?,?,?,?)";
        return jdbcTemplate.update(sql, pm.getRecipeId(), pm.getModelName(), pm.getPrice(), pm.getModelImage(), pm.getDescription());
    }

    public int update(ProductModel pm) {
        String sql = "UPDATE PRODUCT_MODEL SET Recipe_id=?, Model_name=?, Price=?, Model_image=?, Description=? WHERE Model_id=?";
        return jdbcTemplate.update(sql, pm.getRecipeId(), pm.getModelName(), pm.getPrice(), pm.getModelImage(), pm.getDescription(), pm.getModelId());
    }

    public int delete(int id) {
        String sql = "DELETE FROM PRODUCT_MODEL WHERE Model_id=?";
        return jdbcTemplate.update(sql, id);
    }
}