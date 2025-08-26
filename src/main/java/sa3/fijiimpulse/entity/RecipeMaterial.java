package sa3.fijiimpulse.entity;

import java.math.BigDecimal;

public class RecipeMaterial {
    private Long recipeId;
    private Long materialId;
    private BigDecimal quantity;

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public RecipeMaterial(Long recipeId, Long materialId, BigDecimal quantity) {
        this.recipeId = recipeId;
        this.materialId = materialId;
        this.quantity = quantity;
    }
}

