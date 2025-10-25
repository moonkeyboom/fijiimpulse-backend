package sa3.fijiimpulse.entity;

import java.math.BigDecimal;

public class ProductModel {
    private int modelId;
    private int recipeId;
    private String modelName;
    private BigDecimal price;

    // เปลี่ยนจาก byte[] เป็น String สำหรับเก็บชื่อไฟล์หรือ path
    private String modelImagePath;

    private String description;

    public ProductModel() {
    }

    public ProductModel(int modelId, int recipeId, String modelName, BigDecimal price, String modelImagePath, String description) {
        this.modelId = modelId;
        this.recipeId = recipeId;
        this.modelName = modelName;
        this.price = price;
        this.modelImagePath = modelImagePath;
        this.description = description;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getModelImagePath() {
        return modelImagePath;
    }

    public void setModelImagePath(String modelImagePath) {
        this.modelImagePath = modelImagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "modelId=" + modelId +
                ", recipeId=" + recipeId +
                ", modelName='" + modelName + '\'' +
                ", price=" + price +
                ", modelImagePath='" + modelImagePath + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
