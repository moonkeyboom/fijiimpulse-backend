package sa3.fijiimpulse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.service.OrderService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ==================== Use Case 2M: ตรวจสอบและยืนยันหลักฐานการชำระเงิน ====================
    
    @GetMapping("/pending-payment")
    public ResponseEntity<List<Order>> getPendingPaymentOrders() {
        List<Order> orders = orderService.getPendingPaymentOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}/payment")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable long orderId) {
        Payment payment = orderService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{orderId}/approve")
    public ResponseEntity<String> approvePayment(@PathVariable long orderId) {
        boolean success = orderService.approvePayment(orderId);

        if (success) {
            return ResponseEntity.ok("อนุมัติหลักฐานการชำระเงินสำเร็จ");
        } else {
            return ResponseEntity.status(500).body("อนุมัติไม่สำเร็จ");
        }
    }

    @PostMapping("/{orderId}/reject")
    public ResponseEntity<String> rejectPayment(@PathVariable long orderId) {
        boolean success = orderService.rejectPayment(orderId);

        if (success) {
            return ResponseEntity.ok("ปฏิเสธหลักฐานการชำระเงินสำเร็จ");
        } else {
            return ResponseEntity.status(500).body("ปฏิเสธไม่สำเร็จ");
        }
    }

    // ==================== Use Case 3M: ตรวจสอบทรัพยากร ====================

    @PostMapping("/{orderId}/check-materials")
    public ResponseEntity<Map<String, Object>> checkMaterials(@PathVariable long orderId) {
        Map<String, Object> result = orderService.checkMaterialsForOrder(orderId);
        return ResponseEntity.ok(result);
    }
}
