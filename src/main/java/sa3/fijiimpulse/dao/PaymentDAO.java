package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Payment;

import java.util.List;

@Repository
public class PaymentDAO {
    private final JdbcTemplate jdbcTemplate;

    public PaymentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Payment> findAll() {
        String sql = "SELECT * FROM PAYMENT";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Payment p = new Payment();
            p.setPaymentId(rs.getLong("Payment_id"));
            p.setOrderId(rs.getLong("Order_id"));
            p.setTotalAmount(rs.getBigDecimal("Total_amount"));
            p.setPaymentDate(rs.getTimestamp("Payment_date"));
            p.setPaymentReceipt(rs.getString("Payment_receipt")); // path
            return p;
        });
    }

    public Payment findById(long paymentId) {
        String sql = "SELECT * FROM PAYMENT WHERE Payment_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{paymentId}, (rs, rowNum) -> {
            Payment p = new Payment();
            p.setPaymentId(rs.getLong("Payment_id"));
            p.setOrderId(rs.getLong("Order_id"));
            p.setTotalAmount(rs.getBigDecimal("Total_amount"));
            p.setPaymentDate(rs.getTimestamp("Payment_date"));
            p.setPaymentReceipt(rs.getString("Payment_receipt")); // path
            return p;
        });
    }

//    public int insert(Payment payment) {
//        String sql = "INSERT INTO PAYMENT (Order_id, Total_amount, Payment_date, Payment_receipt) VALUES (?,?,?,?)";
//        return jdbcTemplate.update(sql,
//                payment.getOrderId(),
//                payment.getTotalAmount(),
//                payment.getPaymentDate(),
//                payment.getPaymentReceipt()
//        );
//    }

    public int insert(Payment payment) {
        String sql = "INSERT INTO PAYMENT (Order_id, Total_amount, Payment_date, Payment_receipt) VALUES (?,?,?,?)";
        return jdbcTemplate.update(sql,
                payment.getOrderId(),
                payment.getTotalAmount(),
                payment.getPaymentDate(),
                payment.getPaymentReceipt() // เก็บ path
        );
    }
//    public int update(Payment payment) {
//        String sql = "UPDATE PAYMENT SET Order_id=?, Total_amount=?, Payment_date=?, Payment_receipt=? WHERE Payment_id=?";
//        return jdbcTemplate.update(sql,
//                payment.getOrderId(),
//                payment.getTotalAmount(),
//                payment.getPaymentDate(),
//                payment.getPaymentReceipt(),
//                payment.getPaymentId()
//        );
//    }

    public int update(Payment payment) {
        String sql = "UPDATE PAYMENT SET Total_amount=?, Payment_date=?, Payment_receipt=? WHERE Order_id=?";
        return jdbcTemplate.update(sql,
                payment.getTotalAmount(),
                payment.getPaymentDate(),
                payment.getPaymentReceipt(),
                payment.getOrderId()
        );
    }

    public int delete(long paymentId) {
        String sql = "DELETE FROM PAYMENT WHERE Payment_id=?";
        return jdbcTemplate.update(sql, paymentId);
    }

    public Payment findByOrderId(long orderId) {
        String sql = "SELECT * FROM PAYMENT WHERE Order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, rs -> {
            if (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getLong("Payment_id"));
                p.setOrderId(rs.getLong("Order_id"));
                p.setTotalAmount(rs.getBigDecimal("Total_amount"));
                p.setPaymentDate(rs.getTimestamp("Payment_date"));
                p.setPaymentReceipt(rs.getString("Payment_receipt")); // path
                return p;
            }
            return null;
        });
    }

}