package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService ordersService) {
        this.orderService = ordersService;
    }

    // Step 4: สร้าง Order (Checkout)
    @PostMapping
    public int createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    // ดู Order ทั้งหมด
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // ดู Order ตาม ID
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable long id) {
        return orderService.getOrderById(id);
    }
}
