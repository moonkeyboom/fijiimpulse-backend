package sa3.fijiimpulse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.ProductItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductItemDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ProductItemDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<ProductItem> productItemRowMapper = new RowMapper<ProductItem>() {
        @Override
        public ProductItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductItem item = new ProductItem();
            item.setSerialNo(rs.getString("serial_no"));
            item.setModelId(rs.getInt("model_id"));
            item.setOrderId(rs.getObject("order_id") != null ? rs.getLong("order_id") : null); // nullable
            return item;
        }
    };

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

    // หา ProductItem ที่ยังไม่ถูก assign ให้ Order (orderId IS NULL)
    public List<ProductItem> findAvailableByModelId(int modelId) {
        String sql = "SELECT * FROM PRODUCT_ITEM WHERE Model_id = ? AND Order_id IS NULL";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductItem.class), modelId);
    }

    public List<ProductItem> findByOrderId(long orderId) {
        String sql = "SELECT * FROM product_item WHERE order_id = ?";
        return jdbcTemplate.query(sql, productItemRowMapper, orderId);
    }
}