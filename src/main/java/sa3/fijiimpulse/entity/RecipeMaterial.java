package sa3.fijiimpulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class RecipeMaterial {
    private int recipeId;
    private int materialId;
    private int requiredQuantity;

    public RecipeMaterial(int recipeId, int materialId, int requiredQuantity) {
        this.recipeId = recipeId;
        this.materialId = materialId;
        this.requiredQuantity = requiredQuantity;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    @Override
    public String toString() {
        return "RecipeMaterial{" +
                "recipeId=" + recipeId +
                ", materialId=" + materialId +
                ", requiredQuantity=" + requiredQuantity +
                '}';
    }
}