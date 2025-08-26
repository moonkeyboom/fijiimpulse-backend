package sa3.fijiimpulse.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Address;
import sa3.fijiimpulse.entity.Order;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    public int save(Order o) {
        String sql = "INSERT INTO orders (user_id, order_status, recipient_name, house_address, street_name, sub_district, district, province, postal_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Address a = o.getAddress();
        return jdbcTemplate.update(sql, o.getUserId(), o.getOrderStatus(),
                a.getRecipientName(), a.getHouseAddress(), a.getStreetName(),
                a.getSubDistrict(), a.getDistrict(), a.getProvince(), a.getPostalCode());
    }

    public Order findById(Long id) {
        String sql = "SELECT * FROM orders WHERE order_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Address a = new Address(
                    rs.getString("recipient_name"),
                    rs.getString("house_address"),
                    rs.getString("street_name"),
                    rs.getString("sub_district"),
                    rs.getString("district"),
                    rs.getString("province"),
                    rs.getString("postal_code")
            );
            return new Order(
                    rs.getLong("order_id"),
                    rs.getLong("user_id"),
                    rs.getTimestamp("order_date").toLocalDateTime(),
                    rs.getString("order_status"),
                    a
            );
        }, id);
    }

}