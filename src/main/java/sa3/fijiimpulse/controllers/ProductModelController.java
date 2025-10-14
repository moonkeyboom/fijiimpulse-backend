package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sa3.fijiimpulse.entity.ProductModel;
import sa3.fijiimpulse.service.ProductModelService;

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
}
