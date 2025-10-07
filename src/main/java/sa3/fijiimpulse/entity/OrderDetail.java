package sa3.fijiimpulse.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private long orderDetailId;
    private long orderId;
    private int modelId;
    private int orderQuantity;
    private BigDecimal totalPrice;
}
