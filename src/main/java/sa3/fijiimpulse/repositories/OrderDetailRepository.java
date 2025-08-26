package sa3.fijiimpulse.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.OrderDetail;

import java.util.List;

@Repository
public class OrderDetailRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderDetailRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public int save(OrderDetail od) {
        String sql = "INSERT INTO order_details (order_id, serial_no, quantity, total_price) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, od.getOrderId(), od.getSerialNo(), od.getQuantity(), od.getTotalPrice());
    }

    public int update(OrderDetail od) {
        String sql = "UPDATE order_details SET quantity=?, total_price=? WHERE order_id=? AND serial_no=?";
        return jdbcTemplate.update(sql, od.getQuantity(), od.getTotalPrice(), od.getOrderId(), od.getSerialNo());
    }

    public int delete(Long orderId, String serialNo) {
        String sql = "DELETE FROM order_details WHERE order_id=? AND serial_no=?";
        return jdbcTemplate.update(sql, orderId, serialNo);
    }

    public OrderDetail findById(Long orderId, String serialNo) {
        String sql = "SELECT * FROM order_details WHERE order_id=? AND serial_no=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        new OrderDetail(rs.getLong("order_id"),
                                rs.getString("serial_no"),
                                rs.getInt("quantity"),
                                rs.getBigDecimal("total_price")),
                orderId, serialNo);
    }

    public List<OrderDetail> findAll() {
        String sql = "SELECT * FROM order_details";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrderDetail(rs.getLong("order_id"),
                        rs.getString("serial_no"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("total_price")));
    }
}
