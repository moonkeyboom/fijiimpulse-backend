package sa3.fijiimpulse.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
