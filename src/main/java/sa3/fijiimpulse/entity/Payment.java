package sa3.fijiimpulse.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Payment {
    private long paymentId;
    private long orderId;
    private BigDecimal totalAmount;
    private java.sql.Timestamp paymentDate;
    private String paymentReceipt;

    public Payment() {
    }

    public Payment(long paymentId, long orderId, BigDecimal totalAmount, Timestamp paymentDate, String paymentReceipt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
        this.paymentReceipt = paymentReceipt;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentReceipt() {
        return paymentReceipt;
    }

    public void setPaymentReceipt(String paymentReceipt) {
        this.paymentReceipt = paymentReceipt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", totalAmount=" + totalAmount +
                ", paymentDate=" + paymentDate +
                ", paymentReceipt='" + paymentReceipt + '\'' +
                '}';
    }
}
