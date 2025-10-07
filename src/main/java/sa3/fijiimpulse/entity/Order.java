package sa3.fijiimpulse.entity;

import sa3.fijiimpulse.service.enums.OrderStatus;

import java.time.LocalDateTime;

public class Order {
    private Long orderId;
    private Long userId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address; // composite attribute

    public Order() {}

    public Order(Long orderId, Long userId, LocalDateTime orderDate, String orderStatus, Address address) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.orderStatus = OrderStatus.NOT_CREATED;
        this.address = address;
    }

    // getters & setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
}