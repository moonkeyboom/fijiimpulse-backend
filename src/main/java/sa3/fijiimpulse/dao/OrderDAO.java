package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Order;

import java.util.List;

@Repository
public class OrderDAO {
    private final JdbcTemplate jdbcTemplate;

    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM ORDERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Order(
                rs.getLong("Order_id"),
                rs.getInt("User_id"),
                rs.getString("Order_status"),
                rs.getString("Recipient_name"),
                rs.getString("Phone_number"),
                rs.getString("District"),
                rs.getString("House_address"),
                rs.getString("Sub_district"),
                rs.getString("Street_name"),
                rs.getString("Province"),
                rs.getString("Postal_code"),
                rs.getTimestamp("Order_date"),
                rs.getBigDecimal("Grand_total_price")
        ));
    }

    public Order findById(long id) {
        String sql = "SELECT * FROM ORDERS WHERE Order_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Order(
                rs.getLong("Order_id"),
                rs.getInt("User_id"),
                rs.getString("Order_status"),
                rs.getString("Recipient_name"),
                rs.getString("Phone_number"),
                rs.getString("District"),
                rs.getString("House_address"),
                rs.getString("Sub_district"),
                rs.getString("Street_name"),
                rs.getString("Province"),
                rs.getString("Postal_code"),
                rs.getTimestamp("Order_date"),
                rs.getBigDecimal("Grand_total_price")
        ));
    }

    public int insert(Order o) {
        String sql = "INSERT INTO ORDERS (User_id, Order_status, Recipient_name, Phone_number, District, House_address, Sub_district, Street_name, Province, Postal_code, Grand_total_price) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                o.getUserId(),
                o.getOrderStatus(),
                o.getRecipientName(),
                o.getPhoneNumber(),
                o.getDistrict(),
                o.getHouseAddress(),
                o.getSubDistrict(),
                o.getStreetName(),
                o.getProvince(),
                o.getPostalCode(),
                o.getGrandTotalPrice()
        );
    }

    public int update(Order o) {
        String sql = "UPDATE ORDERS SET User_id=?, Order_status=?, Recipient_name=?, Phone_number=?, District=?, House_address=?, Sub_district=?, Street_name=?, Province=?, Postal_code=?, Grand_total_price=? WHERE Order_id=?";
        return jdbcTemplate.update(sql,
                o.getUserId(),
                o.getOrderStatus(),
                o.getRecipientName(),
                o.getPhoneNumber(),
                o.getDistrict(),
                o.getHouseAddress(),
                o.getSubDistrict(),
                o.getStreetName(),
                o.getProvince(),
                o.getPostalCode(),
                o.getGrandTotalPrice(),
                o.getOrderId()
        );
    }

    public int delete(long id) {
        String sql = "DELETE FROM ORDERS WHERE Order_id=?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Order> findByUserId(int userId) {
        String sql = "SELECT * FROM ORDERS WHERE User_id=?";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> new Order(
                rs.getLong("Order_id"),
                rs.getInt("User_id"),
                rs.getString("Order_status"),
                rs.getString("Recipient_name"),
                rs.getString("Phone_number"),
                rs.getString("District"),
                rs.getString("House_address"),
                rs.getString("Sub_district"),
                rs.getString("Street_name"),
                rs.getString("Province"),
                rs.getString("Postal_code"),
                rs.getTimestamp("Order_date"),
                rs.getBigDecimal("Grand_total_price")
        ));
    }

    // Use Case 2M: ดึงคำสั่งซื้อตามสถานะ (เช่น "รอตรวจสอบหลักฐานการชำระเงิน")
    public List<Order> findByOrderStatus(String status) {
        String sql = "SELECT * FROM ORDERS WHERE Order_status=?";
        return jdbcTemplate.query(sql, new Object[]{status}, (rs, rowNum) -> new Order(
                rs.getLong("Order_id"),
                rs.getInt("User_id"),
                rs.getString("Order_status"),
                rs.getString("Recipient_name"),
                rs.getString("Phone_number"),
                rs.getString("District"),
                rs.getString("House_address"),
                rs.getString("Sub_district"),
                rs.getString("Street_name"),
                rs.getString("Province"),
                rs.getString("Postal_code"),
                rs.getTimestamp("Order_date"),
                rs.getBigDecimal("Grand_total_price")
        ));
    }

    // Use Case 2M: อัปเดตสถานะคำสั่งซื้อ
    public int updateOrderStatus(long orderId, String newStatus) {
        String sql = "UPDATE ORDERS SET Order_status=? WHERE Order_id=?";
        return jdbcTemplate.update(sql, newStatus, orderId);
    }

}