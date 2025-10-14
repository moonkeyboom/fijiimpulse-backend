package sa3.fijiimpulse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.RecipeMaterialDAO;

import java.util.Map;

@Service
public class RecipeMaterialService {
    private final RecipeMaterialDAO recipeMaterialDAO;

    public RecipeMaterialService(RecipeMaterialDAO recipeMaterialDAO) {
        this.recipeMaterialDAO = recipeMaterialDAO;
    }

    public Map<Integer, Map<String, Object>> getModelRecipe(int model_id) {
        return recipeMaterialDAO.findByModelId(model_id);
    }

}
