package sa3.fijiimpulse.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.ProductModel;

import java.util.List;

@Repository
public class ProductModelRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductModelRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(ProductModel pm) {
        String sql = "INSERT INTO product_models (model_name, price) VALUES (?, ?)";
        return jdbcTemplate.update(sql, pm.getModelName(), pm.getPrice());
    }

    public int update(ProductModel pm) {
        String sql = "UPDATE product_models SET model_name=?, price=? WHERE model_id=?";
        return jdbcTemplate.update(sql, pm.getModelName(), pm.getPrice(), pm.getModelId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM product_models WHERE model_id=?";
        return jdbcTemplate.update(sql, id);
    }

    public ProductModel findById(Long id) {
        String sql = "SELECT * FROM product_models WHERE model_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new ProductModel(rs.getLong("model_id"),
                        rs.getString("model_name"),
                        rs.getBigDecimal("price")), id);
    }

    public List<ProductModel> findAll() {
        String sql = "SELECT * FROM product_models";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ProductModel(rs.getLong("model_id"),
                        rs.getString("model_name"),
                        rs.getBigDecimal("price")));
    }
}