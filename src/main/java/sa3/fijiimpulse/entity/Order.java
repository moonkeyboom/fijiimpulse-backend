package sa3.fijiimpulse.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Order {
    private long orderId;
    private int userId;
    private String orderStatus;
    private String recipientName;
    private String phoneNumber;
    private String district;
    private String houseAddress;
    private String subDistrict;
    private String streetName;
    private String province;
    private String postalCode;
    private java.sql.Timestamp orderDate;
    private BigDecimal grandTotalPrice;

    public Order(long orderId, int userId, String orderStatus, String recipientName, String phoneNumber, String district, String houseAddress, String subDistrict, String streetName, String province, String postalCode, Timestamp orderDate, BigDecimal grandTotalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.district = district;
        this.houseAddress = houseAddress;
        this.subDistrict = subDistrict;
        this.streetName = streetName;
        this.province = province;
        this.postalCode = postalCode;
        this.orderDate = orderDate;
        this.grandTotalPrice = grandTotalPrice;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getGrandTotalPrice() {
        return grandTotalPrice;
    }

    public void setGrandTotalPrice(BigDecimal grandTotalPrice) {
        this.grandTotalPrice = grandTotalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", orderStatus='" + orderStatus + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", district='" + district + '\'' +
                ", houseAddress='" + houseAddress + '\'' +
                ", subDistrict='" + subDistrict + '\'' +
                ", streetName='" + streetName + '\'' +
                ", province='" + province + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", orderDate=" + orderDate +
                ", grandTotalPrice=" + grandTotalPrice +
                '}';
    }
}
