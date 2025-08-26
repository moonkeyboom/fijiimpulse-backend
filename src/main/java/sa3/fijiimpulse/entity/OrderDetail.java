package sa3.fijiimpulse.entity;

import java.math.BigDecimal;

public class OrderDetail {
    private Long orderId;
    private String serialNo;
    private int quantity;
    private BigDecimal totalPrice;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getSerialNo() { return serialNo; }
    public void setSerialNo(String serialNo) { this.serialNo = serialNo; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public OrderDetail(Long orderId, String serialNo, int quantity, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.serialNo = serialNo;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}

