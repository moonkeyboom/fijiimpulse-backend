package sa3.fijiimpulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class ProductItem {
    private String serialNo;
    private int modelId;
    private Long orderId;

    public ProductItem(String serialNo, int modelId, Long orderId) {
        this.serialNo = serialNo;
        this.modelId = modelId;
        this.orderId = orderId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "serialNo='" + serialNo + '\'' +
                ", modelId=" + modelId +
                ", orderId=" + orderId +
                '}';
    }
}
