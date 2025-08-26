package sa3.fijiimpulse.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Payment;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class PaymentRepository {
    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public int save(Payment p) {
        String sql = "INSERT INTO payments (order_id, payment_type, total_amount, payment_status, payment_date) VALUES (?, ?, ?, ?, ?)";
        Timestamp ts = p.getPaymentDate() != null ? Timestamp.valueOf(p.getPaymentDate()) : null;
        return jdbcTemplate.update(sql, p.getOrderId(), p.getPaymentType(), p.getTotalAmount(), p.getPaymentStatus(), ts);
    }

    public int update(Payment p) {
        String sql = "UPDATE payments SET payment_type=?, total_amount=?, payment_status=?, payment_date=? WHERE payment_id=?";
        Timestamp ts = p.getPaymentDate() != null ? Timestamp.valueOf(p.getPaymentDate()) : null;
        return jdbcTemplate.update(sql, p.getPaymentType(), p.getTotalAmount(), p.getPaymentStatus(), ts, p.getPaymentId());
    }

    public int delete(Long paymentId) {
        String sql = "DELETE FROM payments WHERE payment_id=?";
        return jdbcTemplate.update(sql, paymentId);
    }

    public Payment findById(Long paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Payment(
                        rs.getLong("payment_id"),
                        rs.getLong("order_id"),
                        rs.getString("payment_type"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("payment_status"),
                        rs.getTimestamp("payment_date") != null ? rs.getTimestamp("payment_date").toLocalDateTime() : null
                ), paymentId);
    }

    public List<Payment> findAll() {
        String sql = "SELECT * FROM payments";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Payment(
                        rs.getLong("payment_id"),
                        rs.getLong("order_id"),
                        rs.getString("payment_type"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("payment_status"),
                        rs.getTimestamp("payment_date") != null ? rs.getTimestamp("payment_date").toLocalDateTime() : null
                ));
    }
}