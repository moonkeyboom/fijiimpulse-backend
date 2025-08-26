package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.entity.Recipe;
import sa3.fijiimpulse.repositories.RecipeRepository;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository repository;

    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public int createRecipe(Recipe r) { return repository.save(r); }
    public int updateRecipe(Recipe r) { return repository.update(r); }
    public int deleteRecipe(Long id) { return repository.delete(id); }
    public Recipe getRecipe(Long id) { return repository.findById(id); }
    public List<Recipe> getAllRecipes() { return repository.findAll(); }
}