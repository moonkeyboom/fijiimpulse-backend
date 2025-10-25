package sa3.fijiimpulse.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.entity.ProductModel;
import sa3.fijiimpulse.service.ProductModelService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/product-model")
public class ProductModelController {

    private final ProductModelService productService;

    public ProductModelController(ProductModelService productService) {
        this.productService = productService;
    }

//    @GetMapping
//    public List<ProductModel> getAllProducts() {
//        return productService.getAllProducts();
//    }
//
//    @GetMapping("/{id}")
//    public ProductModel getProductById(@PathVariable int id) {
//        return productService.getProductById(id);
//    }
    @GetMapping
    public List<ProductModel> getAllProducts() {
        List<ProductModel> products = productService.getAllProducts();
        products.forEach(p -> {
            if (p.getModelImagePath() != null) {
                // สร้าง URL สำหรับ Postman หรือ Browser
                p.setModelImagePath("/product-model/image/" + p.getModelId());
            }
        });
        return products;
    }

    @GetMapping("/{id}")
    public ProductModel getProductById(@PathVariable int id) {
        ProductModel p = productService.getProductById(id);
        if (p != null && p.getModelImagePath() != null) {
            p.setModelImagePath("/product-model/image/" + p.getModelId());
        }
        return p;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadOrUpdateProductModel(
            @RequestParam int recipeId,
            @RequestParam String modelName,
            @RequestParam BigDecimal price,
            @RequestParam String description,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            productService.upsertProductModelWithImage(recipeId, modelName, price, description, file);
            return ResponseEntity.ok("Product model saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to save product model: " + e.getMessage());
        }
    }


    @PutMapping("/upload")
    public ResponseEntity<String> updateProductModelWithImage(
            @RequestParam int modelId,
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

//    getProductModelImage
    @GetMapping("/image/{modelId}")
    public ResponseEntity<byte[]> getProductModelImage(@PathVariable int modelId) {
        try {
            ProductModel productModel = productService.getProductById(modelId);
            if (productModel == null || productModel.getModelImagePath() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                        .body("Product model image not found".getBytes());
            }

            File imgFile = new File(productModel.getModelImagePath());
            if (!imgFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                        .body("Image file not found on server".getBytes());
            }

            byte[] imageBytes = Files.readAllBytes(imgFile.toPath());

            HttpHeaders headers = new HttpHeaders();
            String ext = productModel.getModelImagePath().substring(productModel.getModelImagePath().lastIndexOf(".") + 1).toLowerCase();
            if ("png".equals(ext)) headers.setContentType(MediaType.IMAGE_PNG);
            else if ("jpg".equals(ext) || "jpeg".equals(ext)) headers.setContentType(MediaType.IMAGE_JPEG);
            else headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "Error retrieving product model image: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                    .body(errorMsg.getBytes());
        }
    }

}
