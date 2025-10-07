package sa3.fijiimpulse.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class OrderDetail {
    private long orderDetailId;
    private long orderId;
    private int modelId;
    private int orderQuantity;
    private BigDecimal totalPrice;

    public OrderDetail(long orderDetailId, long orderId, int modelId, int orderQuantity, BigDecimal totalPrice) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.modelId = modelId;
        this.orderQuantity = orderQuantity;
        this.totalPrice = totalPrice;
    }

    public long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
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
