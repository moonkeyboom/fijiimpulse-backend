package sa3.fijiimpulse.entity;

import java.math.BigDecimal;

public class ProductModel {
    private Long modelId;
    private String modelName;
    private BigDecimal price;

    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public ProductModel(Long modelId, String modelName, BigDecimal price) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.price = price;
    }
}
