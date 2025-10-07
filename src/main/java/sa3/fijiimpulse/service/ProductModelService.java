package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.ProductModelDAO;
import sa3.fijiimpulse.entity.ProductModel;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductModelService {

    private final ProductModelDAO productModelDAO;

    public ProductModelService(ProductModelDAO productModelDAO) {
        this.productModelDAO = productModelDAO;
    }

    // --- CRUD METHODS ---

    public List<ProductModel> getAllProducts() {
        return productModelDAO.findAll();
    }

    public ProductModel getProductById(int modelId) {
        return productModelDAO.findById(modelId);
    }

    public int createProduct(ProductModel product) {
        return productModelDAO.insert(product);
    }

    public int updateProduct(ProductModel product) {
        return productModelDAO.update(product);
    }

    public int deleteProduct(int modelId) {
        return productModelDAO.delete(modelId);
    }


    public ProductModel getProductByRecipeId(int recipeId) {
        return productModelDAO.findByRecipeId(recipeId);
    }
}
