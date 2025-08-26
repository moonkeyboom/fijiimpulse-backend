package sa3.fijiimpulse.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Product;

import java.util.List;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Product p) {
        String sql = "INSERT INTO products (serial_no, model_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, p.getSerialNo(), p.getModelId());
    }

    public int update(Product p) {
        String sql = "UPDATE products SET model_id=? WHERE serial_no=?";
        return jdbcTemplate.update(sql, p.getModelId(), p.getSerialNo());
    }

    public int delete(String serialNo) {
        String sql = "DELETE FROM products WHERE serial_no=?";
        return jdbcTemplate.update(sql, serialNo);
    }

    public Product findById(String serialNo) {
        String sql = "SELECT * FROM products WHERE serial_no=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Product(rs.getString("serial_no"), rs.getLong("model_id")), serialNo);
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Product(rs.getString("serial_no"), rs.getLong("model_id")));
    }
}