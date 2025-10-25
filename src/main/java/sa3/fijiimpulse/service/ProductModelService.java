package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.dao.ProductModelDAO;
import sa3.fijiimpulse.entity.ProductModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductModelService {

    private final ProductModelDAO productModelDAO;

    // โฟลเดอร์เก็บรูปสินค้า (absolute path สำหรับเซฟไฟล์)
    private final String uploadDirAbsolute = System.getProperty("user.dir") + "/uploads/product-model-image";
    // โฟลเดอร์ relative สำหรับเก็บใน DB
    private final String uploadDirRelative = "uploads/product-model-image";

    public ProductModelService(ProductModelDAO productModelDAO) {
        this.productModelDAO = productModelDAO;
    }

    // --- CRUD ---
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

    public int deleteProduct(int modelId) throws IOException {
        ProductModel pm = productModelDAO.findById(modelId);
        if (pm != null && pm.getModelImagePath() != null) {
            // ลบไฟล์เก่า
            Path oldFile = Paths.get(System.getProperty("user.dir"), pm.getModelImagePath());
            if (Files.exists(oldFile)) Files.delete(oldFile);
        }
        return productModelDAO.delete(modelId);
    }

    // ================= Upload / Update Product Model with Image =================

    public void saveProductModelWithImage(int recipeId, String modelName, BigDecimal price, String description, MultipartFile file) throws IOException {
        ProductModel productModel = new ProductModel();
        productModel.setRecipeId(recipeId);
        productModel.setModelName(modelName);
        productModel.setPrice(price);
        productModel.setDescription(description);

        if (file != null && !file.isEmpty()) {
            Path uploadPath = Paths.get(uploadDirAbsolute);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            // ตั้งชื่อไฟล์สุ่ม
            String ext = "";
            int dotIndex = file.getOriginalFilename().lastIndexOf('.');
            if (dotIndex >= 0) ext = file.getOriginalFilename().substring(dotIndex);
            String filename = UUID.randomUUID().toString() + ext;

            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            // เก็บ path แบบ relative ใน DB
            productModel.setModelImagePath(uploadDirRelative + "/" + filename);
        }

        productModelDAO.insert(productModel);
    }

    public void updateProductModelWithImage(int modelId, int recipeId, String modelName, BigDecimal price, String description, MultipartFile file) throws IOException {
        ProductModel productModel = productModelDAO.findById(modelId);
        if (productModel == null) throw new IOException("Product model not found");

        productModel.setRecipeId(recipeId);
        productModel.setModelName(modelName);
        productModel.setPrice(price);
        productModel.setDescription(description);

        if (file != null && !file.isEmpty()) {
            Path uploadPath = Paths.get(uploadDirAbsolute);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            // ลบไฟล์เก่า
            if (productModel.getModelImagePath() != null) {
                Path oldFile = Paths.get(System.getProperty("user.dir"), productModel.getModelImagePath());
                if (Files.exists(oldFile)) Files.delete(oldFile);
            }

            // ตั้งชื่อไฟล์ใหม่
            String ext = "";
            int dotIndex = file.getOriginalFilename().lastIndexOf('.');
            if (dotIndex >= 0) ext = file.getOriginalFilename().substring(dotIndex);
            String filename = UUID.randomUUID().toString() + ext;

            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            // เก็บ path แบบ relative ใน DB
            productModel.setModelImagePath(uploadDirRelative + "/" + filename);
        }

        productModelDAO.update(productModel);
    }

    public void upsertProductModelWithImage(int recipeId, String modelName, BigDecimal price, String description, MultipartFile file) throws IOException {
        // เช็คว่ามี ProductModel ของ recipeId นี้อยู่แล้วหรือไม่
        ProductModel productModel = productModelDAO.findByRecipeId(recipeId);

        if (productModel == null) {
            // insert ใหม่
            saveProductModelWithImage(recipeId, modelName, price, description, file);
        } else {
            // update ของเดิม
            updateProductModelWithImage(productModel.getModelId(), recipeId, modelName, price, description, file);
        }
    }

}
