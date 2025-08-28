package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.RecipeMaterial;
import sa3.fijiimpulse.service.RecipeMaterialService;

import java.util.List;

@RestController
@RequestMapping("/recipe-materials")
public class RecipeMaterialController {
    private final RecipeMaterialService service;

    public RecipeMaterialController(RecipeMaterialService service) {
        this.service = service;
    }

    @PostMapping
    public String create(@RequestBody RecipeMaterial rm) {
        service.create(rm); return "Created";
    }

    @PutMapping("/{recipeId}/{materialId}")
    public String update(@PathVariable Long recipeId, @PathVariable Long materialId, @RequestBody RecipeMaterial rm) {
        rm.setRecipeId(recipeId);
        rm.setMaterialId(materialId);
        service.update(rm);
        return "Updated";
    }

    @DeleteMapping("/{recipeId}/{materialId}")
    public String delete(@PathVariable Long recipeId, @PathVariable Long materialId) {
        service.delete(recipeId, materialId);
        return "Deleted";
    }

    @GetMapping("/{recipeId}/{materialId}")
    public RecipeMaterial get(@PathVariable Long recipeId, @PathVariable Long materialId) {
        return service.get(recipeId, materialId);
    }

    @GetMapping
    public List<RecipeMaterial> getAll() {
        return service.getAll();
    }
}