package sa3.fijiimpulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

import java.math.BigDecimal;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class ProductModel {
    private int modelId;
    private int recipeId;
    private String modelName;
    private BigDecimal price;

    @Column(name = "Model_image")
    private byte[] modelImage; // ใช้ byte[] เพื่อเก็บภาพใน DB

    private String description;

    public ProductModel() {
    }

    public ProductModel(int modelId, int recipeId, String modelName, BigDecimal price, byte[] modelImage, String description) {
        this.modelId = modelId;
        this.recipeId = recipeId;
        this.modelName = modelName;
        this.price = price;
        this.modelImage = modelImage;
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

    public byte[] getModelImage() {
        return modelImage;
    }

    public void setModelImage(byte[] modelImage) {
        this.modelImage = modelImage;
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
                ", modelImage=" + (modelImage != null ? "[BLOB data]" : "null") +
                ", description='" + description + '\'' +
                '}';
    }
}


