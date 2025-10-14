package sa3.fijiimpulse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.ProductItem;

import java.util.List;

@Repository
public class ProductItemDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // INSERT
    public int save(ProductItem item) {
        String sql = "INSERT INTO PRODUCT_ITEM (Serial_no, Model_id, Order_id) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, item.getSerialNo(), item.getModelId(), item.getOrderId());
    }

    // SELECT by Serial_no
    public ProductItem findBySerialNo(String serialNo) {
        String sql = "SELECT * FROM PRODUCT_ITEM WHERE Serial_no = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ProductItem.class), serialNo);
    }

    // SELECT All
    public List<ProductItem> findAll() {
        String sql = "SELECT * FROM PRODUCT_ITEM";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductItem.class));
    }

    // UPDATE Order_id (เชื่อมกับ Order ใหม่)
    public int updateOrder(String serialNo, Long orderId) {
        String sql = "UPDATE PRODUCT_ITEM SET Order_id = ? WHERE Serial_no = ?";
        return jdbcTemplate.update(sql, orderId, serialNo);
    }

    // DELETE
    public int delete(String serialNo) {
        String sql = "DELETE FROM PRODUCT_ITEM WHERE Serial_no = ?";
        return jdbcTemplate.update(sql, serialNo);
    }
}