package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.ProductItem;

import java.util.List;

@Repository
public class ProductItemDAO {
    private final JdbcTemplate jdbcTemplate;

    public ProductItemDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductItem> findAll() {
        String sql = "SELECT * FROM PRODUCT_ITEM";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProductItem(
                rs.getString("Serial_no"),
                rs.getInt("Model_id")
        ));
    }

    public ProductItem findById(String serialNo) {
        String sql = "SELECT * FROM PRODUCT_ITEM WHERE Serial_no=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{serialNo}, (rs, rowNum) -> new ProductItem(
                rs.getString("Serial_no"),
                rs.getInt("Model_id")
        ));
    }

    public int insert(ProductItem pi) {
        String sql = "INSERT INTO PRODUCT_ITEM (Serial_no, Model_id) VALUES (?,?)";
        return jdbcTemplate.update(sql, pi.getSerialNo(), pi.getModelId());
    }

    public int update(ProductItem pi) {
        String sql = "UPDATE PRODUCT_ITEM SET Model_id=? WHERE Serial_no=?";
        return jdbcTemplate.update(sql, pi.getModelId(), pi.getSerialNo());
    }

    public int delete(String serialNo) {
        String sql = "DELETE FROM PRODUCT_ITEM WHERE Serial_no=?";
        return jdbcTemplate.update(sql, serialNo);
    }

    // Use Case 3M: ดึง Product Items สำหรับ Order นี้
    public List<ProductItem> findByOrderId(long orderId) {
        String sql = "SELECT * FROM PRODUCT_ITEM WHERE Order_id=?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) -> new ProductItem(
                rs.getString("Serial_no"),
                rs.getInt("Model_id")
        ));
    }
}