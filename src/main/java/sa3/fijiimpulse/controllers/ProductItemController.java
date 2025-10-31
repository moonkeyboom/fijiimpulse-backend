package sa3.fijiimpulse.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.service.MaterialService;
import sa3.fijiimpulse.service.ProductItemService;
import sa3.fijiimpulse.service.RecipeMaterialService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product-items")
public class ProductItemController {
    @Autowired
    private ProductItemService productItemService;
    @Autowired
    private MaterialController materialController;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private RecipeMaterialService recipeMaterialService;

    // POST: Create new ProductItem
    @PostMapping
    public String createProductItem(@RequestBody ProductItem item) {
        productItemService.save(item);
        return "ProductItem created successfully!";
    }

    // GET: Get all
    @GetMapping
    public List<ProductItem> getAll() {
        return productItemService.findAll();
    }

    // GET: Get by SerialNo
    @GetMapping("/{serialNo}")
    public ProductItem getBySerialNo(@PathVariable String serialNo) {
        return productItemService.findBySerialNo(serialNo);
    }

    // PUT: Update OrderId
    @PutMapping("/{serialNo}/order/{orderId}")
    public String updateOrder(@PathVariable String serialNo, @PathVariable Long orderId) {
        productItemService.updateOrder(serialNo, orderId);
        return "Order updated for ProductItem: " + serialNo;
    }

    // DELETE: Remove ProductItem
    @DeleteMapping("/{serialNo}")
    public String delete(@PathVariable String serialNo) {
        productItemService.delete(serialNo);
        return "ProductItem deleted successfully!";
    }

    @PostMapping("/adjust-order-items")
    public String adjustProductItemsForOrder(
            @RequestParam long orderId,
            @RequestParam int modelId,
            @RequestParam int quantity) {
        productItemService.adjustProductItemsForOrder(modelId, orderId, quantity);
        return "Adjusted ProductItems for Order ID " + orderId +
                " (Model ID: " + modelId + ", Desired Quantity: " + quantity + ")";
    }
//    POST http://localhost:8080/product-items/adjust-items?orderId=10&modelId=2&quantity=2
//    GET http://localhost:8081/orders/10/items

    @PostMapping("/adjust-user-items")
    public String adjustProductItemsForUser(
            @RequestParam int userId,
            @RequestParam int modelId,
            @RequestParam int quantity,
            @RequestBody Map<String, String> address) {
        System.out.println(address);
        productItemService.adjustProductItemsForUser(modelId, userId, quantity, address);
        return "Adjusted ProductItems for User ID " + userId +
                " (Model ID: " + modelId + ", Desired Quantity: " + quantity + ")";
    }

    @PostMapping("/create-multiple")
    public void createMultipleProductItems(@RequestBody Map<Integer, Integer> productItems) {
        for (Map.Entry<Integer, Integer> entry : productItems.entrySet()) {
            int modelId = entry.getKey();
            int qnt = entry.getValue();
            for (int i=0;i<qnt;i++) {
                productItemService.addProductItem(modelId);
                Map<Integer, Map<String, Object>> recipe = recipeMaterialService.getModelRecipe(modelId);
                for (Map.Entry<Integer, Map<String, Object>> modelEntry : recipe.entrySet()) {
                    int matId = modelEntry.getKey();
                    Object reqObj = modelEntry.getValue().get("required_quantity");
                    int requireQnt = ((Number) reqObj).intValue();
                    materialService.reduceMaterial(matId, requireQnt);
                }
            }
        }
    }

    // ✅ ดึง ProductItem ทั้งหมดของ User (รวมทุก Order)
    @GetMapping("/user/{userId}")
    public List<ProductItem> getProductItemsByUserId(@PathVariable int userId) {
        return productItemService.getProductItemsByUserId(userId);
    }

    @GetMapping("/items/{orderId}")
    public List<Map<String, Object>> getProductItemsByOrderId(@PathVariable int orderId) {
        return productItemService.getProductListFromOrderId(orderId);
    }
}