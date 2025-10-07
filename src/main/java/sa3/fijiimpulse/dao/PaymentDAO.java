package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Payment;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PaymentDAO {
    private final JdbcTemplate jdbcTemplate;

    public PaymentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Payment> findAll() {
        String sql = "SELECT * FROM PAYMENT";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Payment(
                rs.getLong("Payment_id"),
                rs.getLong("Order_id"),
                rs.getBigDecimal("Total_amount"),
                rs.getTimestamp("Payment_date"),
                rs.getString("Payment_receipt")
        ));
    }

    public Payment findById(long paymentId) {
        String sql = "SELECT * FROM PAYMENT WHERE Payment_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{paymentId}, (rs, rowNum) -> new Payment(
                rs.getLong("Payment_id"),
                rs.getLong("Order_id"),
                rs.getBigDecimal("Total_amount"),
                rs.getTimestamp("Payment_date"),
                rs.getString("Payment_receipt")
        ));
    }

    public int insert(Payment payment) {
        String sql = "INSERT INTO PAYMENT (Order_id, Total_amount, Payment_date, Payment_receipt) VALUES (?,?,?,?)";
        return jdbcTemplate.update(sql,
                payment.getOrderId(),
                payment.getTotalAmount(),
                payment.getPaymentDate(),
                payment.getPaymentReceipt()
        );
    }

    public int update(Payment payment) {
        String sql = "UPDATE PAYMENT SET Order_id=?, Total_amount=?, Payment_date=?, Payment_receipt=? WHERE Payment_id=?";
        return jdbcTemplate.update(sql,
                payment.getOrderId(),
                payment.getTotalAmount(),
                payment.getPaymentDate(),
                payment.getPaymentReceipt(),
                payment.getPaymentId()
        );
    }

    public int delete(long paymentId) {
        String sql = "DELETE FROM PAYMENT WHERE Payment_id=?";
        return jdbcTemplate.update(sql, paymentId);
    }

    public Payment findByOrderId(long orderId) {
        String sql = "SELECT * FROM PAYMENT WHERE Order_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{orderId}, (rs, rowNum) -> new Payment(
                rs.getLong("Payment_id"),
                rs.getLong("Order_id"),
                rs.getBigDecimal("Total_amount"),
                rs.getTimestamp("Payment_date"),
                rs.getString("Payment_receipt")
        ));
    }
}