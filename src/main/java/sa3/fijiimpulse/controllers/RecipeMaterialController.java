package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sa3.fijiimpulse.service.RecipeMaterialService;

import java.util.Map;


@RestController
@RequestMapping("api/recipe-material")
public class RecipeMaterialController {
    private final RecipeMaterialService recipeMaterialService;

    public RecipeMaterialController(RecipeMaterialService recipeMaterialService) {
        this.recipeMaterialService = recipeMaterialService;
    }

    @GetMapping("/{model_id}")
    public Map<Integer, Map<String, Object>> getModelRecipe(@PathVariable int model_id) {
        return recipeMaterialService.getModelRecipe(model_id);
    }
}
