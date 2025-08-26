package sa3.fijiimpulse.service;
import org.springframework.stereotype.Service;
import sa3.fijiimpulse.entity.RecipeMaterial;
import sa3.fijiimpulse.repositories.RecipeMaterialRepository;

import java.util.List;

@Service
public class RecipeMaterialService {
    private final RecipeMaterialRepository repository;

    public RecipeMaterialService(RecipeMaterialRepository repository) { this.repository = repository; }

    public int create(RecipeMaterial rm) { return repository.save(rm); }
    public int update(RecipeMaterial rm) { return repository.update(rm); }
    public int delete(Long recipeId, Long materialId) { return repository.delete(recipeId, materialId); }
    public RecipeMaterial get(Long recipeId, Long materialId) { return repository.findById(recipeId, materialId); }
    public List<RecipeMaterial> getAll() { return repository.findAll(); }
}
