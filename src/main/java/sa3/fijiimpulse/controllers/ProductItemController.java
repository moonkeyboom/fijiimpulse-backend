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

    @PostMapping("/assign")
    public String assignToOrder(@RequestParam int modelId,
                                @RequestParam long orderId,
                                @RequestParam int quantity) {
        productItemService.addProductItemsToOrder(modelId, orderId, quantity);
        return quantity + " ProductItems assigned to Order " + orderId;
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
}