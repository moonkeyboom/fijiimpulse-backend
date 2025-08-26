package sa3.fijiimpulse.entity;

public class Recipe {
    private Long recipeId;
    private Long modelId;

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }

    public Recipe(Long recipeId, Long modelId) {
        this.recipeId = recipeId;
        this.modelId = modelId;
    }
}

