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

    public ProductItem(String serialNo, int modelId) {
        this.serialNo = serialNo;
        this.modelId = modelId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "serialNo='" + serialNo + '\'' +
                ", modelId=" + modelId +
                '}';
    }
}
