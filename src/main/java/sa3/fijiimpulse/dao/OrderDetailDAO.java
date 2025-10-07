package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class OrderDetailDAO {
    private final JdbcTemplate jdbcTemplate;

    public OrderDetailDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrderDetail> findAll() {
        String sql = "SELECT * FROM ORDER_DETAILS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new OrderDetail(
                rs.getLong("Order_detail_id"),
                rs.getLong("Order_id"),
                rs.getInt("Model_id"),
                rs.getInt("Order_quantity"),
                rs.getBigDecimal("Total_price")
        ));
    }

    public OrderDetail findById(long id) {
        String sql = "SELECT * FROM ORDER_DETAILS WHERE Order_detail_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new OrderDetail(
                rs.getLong("Order_detail_id"),
                rs.getLong("Order_id"),
                rs.getInt("Model_id"),
                rs.getInt("Order_quantity"),
                rs.getBigDecimal("Total_price")
        ));
    }

    public int insert(OrderDetail od) {
        String sql = "INSERT INTO ORDER_DETAILS (Order_id, Model_id, Order_quantity, Total_price) VALUES (?,?,?,?)";
        return jdbcTemplate.update(sql, od.getOrderId(), od.getModelId(), od.getOrderQuantity(), od.getTotalPrice());
    }

    public int update(OrderDetail od) {
        String sql = "UPDATE ORDER_DETAILS SET Order_id=?, Model_id=?, Order_quantity=?, Total_price=? WHERE Order_detail_id=?";
        return jdbcTemplate.update(sql, od.getOrderId(), od.getModelId(), od.getOrderQuantity(), od.getTotalPrice(), od.getOrderDetailId());
    }

    public int delete(long id) {
        String sql = "DELETE FROM ORDER_DETAILS WHERE Order_detail_id=?";
        return jdbcTemplate.update(sql, id);
    }

    public List<OrderDetail> findByOrderId(long orderId) {
        String sql = "SELECT * FROM ORDER_DETAILS WHERE Order_id=?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) -> new OrderDetail(
                rs.getLong("Order_detail_id"),
                rs.getLong("Order_id"),
                rs.getInt("Model_id"),
                rs.getInt("Order_quantity"),
                rs.getBigDecimal("Total_price")
        ));
    }

    public OrderDetail findByOrderIdAndModelId(long orderId, int modelId) {
        String sql = "SELECT * FROM ORDER_DETAILS WHERE Order_id=? AND Model_id=?";
        List<OrderDetail> list = jdbcTemplate.query(sql, new Object[]{orderId, modelId},
                (rs, rowNum) -> new OrderDetail(
                        rs.getLong("Order_detail_id"),
                        rs.getLong("Order_id"),
                        rs.getInt("Model_id"),
                        rs.getInt("Order_quantity"),
                        rs.getBigDecimal("Total_price")
                ));
        return list.isEmpty() ? null : list.get(0);
    }

}
