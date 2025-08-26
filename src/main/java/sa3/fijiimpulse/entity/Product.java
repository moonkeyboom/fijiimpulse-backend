package sa3.fijiimpulse.entity;

public class Product {
    private String serialNo;
    private Long modelId;

    public String getSerialNo() { return serialNo; }
    public void setSerialNo(String serialNo) { this.serialNo = serialNo; }

    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }

    public Product(String serialNo, Long modelId) {
        this.serialNo = serialNo;
        this.modelId = modelId;
    }
}

