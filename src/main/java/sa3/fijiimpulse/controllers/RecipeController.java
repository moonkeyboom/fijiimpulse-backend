package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.Recipe;
import sa3.fijiimpulse.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService service;

    public RecipeController(RecipeService service) { this.service = service; }

    @PostMapping
    public String create(@RequestBody Recipe r) {
        service.createRecipe(r); return "Recipe created";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody Recipe r) {
        r.setRecipeId(id); service.updateRecipe(r); return "Recipe updated";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteRecipe(id); return "Recipe deleted";
    }

    @GetMapping("/{id}")
    public Recipe get(@PathVariable Long id) { return service.getRecipe(id); }

    @GetMapping
    public List<Recipe> getAll() { return service.getAllRecipes(); }
}