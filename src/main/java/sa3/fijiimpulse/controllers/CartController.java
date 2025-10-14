package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.OrderDetail;
import sa3.fijiimpulse.service.OrderDetailService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final OrderDetailService orderDetailService;

    public CartController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    

    // Step 2: เพิ่มสินค้าในตะกร้า
    @PostMapping("/add")
    public int addToCart(@RequestParam long orderId,
                         @RequestParam int modelId,
                         @RequestParam int quantity,
                         @RequestParam BigDecimal price) {
        return orderDetailService.addOrUpdateOrderDetail(orderId, modelId, quantity, price);
    }

    // Step 3: ดูตะกร้าของ Order
    @GetMapping("/{orderId}")
    public List<OrderDetail> getCart(@PathVariable long orderId) {
        return orderDetailService.getOrderDetailsByOrderId(orderId);
    }

    // Step 3: แก้ไขจำนวนสินค้าในตะกร้า
    @PutMapping("/{orderDetailId}")
    public int updateCart(@PathVariable long orderDetailId,
                          @RequestParam int quantity,
                          @RequestParam BigDecimal totalPrice) {
        OrderDetail od = orderDetailService.getOrderDetailById(orderDetailId);
        od.setOrderQuantity(quantity);
        od.setTotalPrice(totalPrice);
        return orderDetailService.updateOrderDetail(od);
    }
}