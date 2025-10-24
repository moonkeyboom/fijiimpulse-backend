package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.dao.ProductModelDAO;
import sa3.fijiimpulse.entity.ProductModel;

import java.io.IOException;
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

    // ==================== Upload Product Model with Image ====================

    public void saveProductModelWithImage(int recipeId, String modelName, BigDecimal price, String description, MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes(); // แปลงไฟล์เป็น byte[]

        ProductModel productModel = new ProductModel();
        productModel.setRecipeId(recipeId);
        productModel.setModelName(modelName);
        productModel.setPrice(price);
        productModel.setDescription(description);
        productModel.setModelImage(fileBytes); // เก็บรูปเป็น byte[]

        productModelDAO.insert(productModel);
    }

    public void updateProductModelWithImage(int modelId, int recipeId, String modelName, BigDecimal price, String description, MultipartFile file) throws IOException {
        byte[] fileBytes = file != null ? file.getBytes() : null;

        ProductModel productModel = new ProductModel();
        productModel.setModelId(modelId);
        productModel.setRecipeId(recipeId);
        productModel.setModelName(modelName);
        productModel.setPrice(price);
        productModel.setDescription(description);

        if (fileBytes != null) {
            productModel.setModelImage(fileBytes);
        } else {
            // ถ้าไม่มีไฟล์ใหม่ ให้ใช้รูปเดิม
            ProductModel existing = productModelDAO.findById(modelId);
            productModel.setModelImage(existing.getModelImage());
        }

        productModelDAO.update(productModel);
    }
}
