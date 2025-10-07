package sa3.fijiimpulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class ProductModel {
    private int modelId;
    private int recipeId;
    private String modelName;
    private BigDecimal price;
    private String modelImage;
    private String description;

    public ProductModel(int modelId, int recipeId, String modelName, BigDecimal price, String modelImage, String description) {
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

    public String getModelImage() {
        return modelImage;
    }

    public void setModelImage(String modelImage) {
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
                ", modelImage='" + modelImage + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}


