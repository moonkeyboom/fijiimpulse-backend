package sa3.fijiimpulse.entity;

import java.math.BigDecimal;

public class OrderDetail {
    private Long orderDetailId;
    private Long orderId;
    private Integer modelId;
    private Integer orderQuantity;
    private BigDecimal totalPrice;

    public OrderDetail() {
    }

    public OrderDetail(Long orderDetailId, Long orderId, Integer modelId, Integer orderQuantity, BigDecimal totalPrice) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.modelId = modelId;
        this.orderQuantity = orderQuantity;
        this.totalPrice = totalPrice;
    }

    // Constructor with automatic price calculation
    public OrderDetail(Long orderId, Integer modelId, Integer orderQuantity, BigDecimal unitPrice) {
        this.orderDetailId = null; // Will be auto-generated
        this.orderId = orderId;
        this.modelId = modelId;
        this.orderQuantity = orderQuantity;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(orderQuantity));
    }

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetailId=" + orderDetailId +
                ", orderId=" + orderId +
                ", modelId=" + modelId +
                ", orderQuantity=" + orderQuantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}