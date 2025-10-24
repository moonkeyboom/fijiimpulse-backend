package sa3.fijiimpulse.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.entity.ProductModel;
import sa3.fijiimpulse.service.ProductModelService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/product-model")
public class ProductModelController {

    private final ProductModelService productService;

    public ProductModelController(ProductModelService productService) {
        this.productService = productService;
    }

    // Step 1: แสดงรายการสินค้าทั้งหมด
    @GetMapping
    public List<ProductModel> getAllProducts() {
        return productService.getAllProducts();
    }

    // ดูรายละเอียดสินค้า ตาม Model_id
    @GetMapping("/{id}")
    public ProductModel getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    // ดูสินค้าโดย Recipe_id
    @GetMapping("/recipe/{recipeId}")
    public ProductModel getProductByRecipeId(@PathVariable int recipeId) {
        return productService.getProductByRecipeId(recipeId);
    }

    // ==================== CRUD Operations ====================

    @PostMapping
    public ResponseEntity<String> createProductModel(@RequestBody ProductModel productModel) {
        try {
            productService.createProduct(productModel);
            return ResponseEntity.ok("Product model created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to create product model: " + e.getMessage());
        }
    }

    @PutMapping("/{modelId}")
    public ResponseEntity<String> updateProductModel(@PathVariable int modelId, @RequestBody ProductModel productModel) {
        try {
            productModel.setModelId(modelId);
            productService.updateProduct(productModel);
            return ResponseEntity.ok("Product model updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to update product model: " + e.getMessage());
        }
    }

    @DeleteMapping("/{modelId}")
    public ResponseEntity<String> deleteProductModel(@PathVariable int modelId) {
        try {
            productService.deleteProduct(modelId);
            return ResponseEntity.ok("Product model deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to delete product model: " + e.getMessage());
        }
    }

    // ==================== Upload Product Model with Image ====================

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProductModel(
            @RequestParam int recipeId,
            @RequestParam String modelName,
            @RequestParam BigDecimal price,
            @RequestParam String description,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            productService.saveProductModelWithImage(recipeId, modelName, price, description, file);
            return ResponseEntity.ok("Product model uploaded successfully with image!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to upload product model: " + e.getMessage());
        }
    }

    @PutMapping("/upload/{modelId}")
    public ResponseEntity<String> updateProductModelWithImage(
            @PathVariable int modelId,
            @RequestParam int recipeId,
            @RequestParam String modelName,
            @RequestParam BigDecimal price,
            @RequestParam String description,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            productService.updateProductModelWithImage(modelId, recipeId, modelName, price, description, file);
            return ResponseEntity.ok("Product model updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to update product model: " + e.getMessage());
        }
    }

    // ==================== Image Endpoints ====================

    @GetMapping("/image/{modelId}")
    public ResponseEntity<byte[]> getProductModelImage(@PathVariable int modelId) {
        try {
            ProductModel productModel = productService.getProductById(modelId);
            if (productModel == null || productModel.getModelImage() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                        .body("Product model image not found".getBytes());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(productModel.getModelImage(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "Error retrieving product model image: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                    .body(errorMsg.getBytes());
        }
    }
}
