package sa3.fijiimpulse.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private long paymentId;
    private long orderId;
    private BigDecimal totalAmount;
    private java.sql.Timestamp paymentDate;
    private String paymentReceipt;
}
