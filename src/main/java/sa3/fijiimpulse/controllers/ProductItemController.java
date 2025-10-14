package sa3.fijiimpulse.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.service.ProductItemService;

import java.util.List;

@RestController
@RequestMapping("/product-items")
public class ProductItemController {

    @Autowired
    private ProductItemService productItemService;

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
}