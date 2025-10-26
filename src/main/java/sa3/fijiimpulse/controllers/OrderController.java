package sa3.fijiimpulse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.service.OrderService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // ==================== Use Case: Create Order ====================

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            int result = orderService.createOrder(order);

            if (result > 0) {
                // ดึง Order ที่สร้างใหม่กลับมา (เพื่อให้ได้ orderId)
                Order createdOrder = orderService.getOrderById(order.getOrderId());
                return ResponseEntity.ok(createdOrder);
            } else {
                return ResponseEntity.status(500).body("สร้าง Order ไม่สำเร็จ");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
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

    // ==================== Use Case: Production & Shipping Flow ====================

    @PostMapping("/{orderId}/mark-produced")
    public ResponseEntity<String> markAsProduced(@PathVariable long orderId) {
        boolean success = orderService.markAsProduced(orderId);

        if (success) {
            return ResponseEntity.ok("ทำเครื่องหมายว่าผลิตสำเร็จแล้ว");
        } else {
            return ResponseEntity.status(500).body("อัปเดตสถานะไม่สำเร็จ");
        }
    }

    @PostMapping("/{orderId}/prepare-shipping")
    public ResponseEntity<String> prepareShipping(@PathVariable long orderId) {
        boolean success = orderService.prepareShipping(orderId);

        if (success) {
            return ResponseEntity.ok("เตรียมจัดส่งสินค้าสำเร็จ");
        } else {
            return ResponseEntity.status(500).body("อัปเดตสถานะไม่สำเร็จ");
        }
    }

    @PostMapping("/{orderId}/ship")
    public ResponseEntity<String> shipOrder(@PathVariable long orderId) {
        boolean success = orderService.shipOrder(orderId);

        if (success) {
            return ResponseEntity.ok("จัดส่งสินค้าสำเร็จ");
        } else {
            return ResponseEntity.status(500).body("อัปเดตสถานะไม่สำเร็จ");
        }
    }

    @PostMapping("/{orderId}/confirm-delivery")
    public ResponseEntity<String> confirmDelivery(@PathVariable long orderId) {
        boolean success = orderService.markAsDelivered(orderId);

        if (success) {
            return ResponseEntity.ok("ยืนยันการรับสินค้าสำเร็จ");
        } else {
            return ResponseEntity.status(500).body("อัปเดตสถานะไม่สำเร็จ");
        }
    }

    // ==================== Use Case 3M: ตรวจสอบทรัพยากร ====================

    @PostMapping("/{orderId}/check-materials")
    public ResponseEntity<Map<String, Object>> checkMaterials(@PathVariable long orderId) {
        Map<String, Object> result = orderService.checkMaterialsForOrder(orderId);
        return ResponseEntity.ok(result);
    }

    //======
    // ดึง ProductItem ทั้งหมดของ Order
    @GetMapping("/{orderId}/items")
    public List<ProductItem> getProductItemsByOrderId(@PathVariable long orderId) {
        return orderService.getProductItemsByOrderId(orderId);
    }
}
