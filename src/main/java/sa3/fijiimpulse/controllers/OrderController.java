package sa3.fijiimpulse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.entity.OrderDetail;
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
    @GetMapping("/order/{orderId}/items")
    public List<ProductItem> getProductItemsByOrderId(@PathVariable long orderId) {
        return orderService.getProductItemsByOrderId(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrderByUserId(@PathVariable int userId) {
        return orderService.getOrdersByUserId(userId);
    }

    /**
     * Get all product items for a specific user
     * @param userId User ID
     * @return List of all product items belonging to the user
     */
    @GetMapping("/user/{userId}/items")
    public List<ProductItem> getProductItemsByUserId(@PathVariable int userId) {
        return orderService.getProductItemsByUserId(userId);
    }

    /**
     * Get all order details for a specific user
     * @param userId User ID
     * @return List of all order details belonging to the user
     */
    @GetMapping("/user/{userId}/order-details")
    public List<OrderDetail> getOrderDetailsByUserId(@PathVariable int userId) {
        return orderService.getOrderDetailsByUserId(userId);
    }

    /**
     * Get specific order for a user
     * @param userId User ID
     * @param orderId Order ID
     * @return Order details for the specific user order
     */
    @GetMapping("/user/{userId}/order/{orderId}")
    public ResponseEntity<Order> getUserOrderById(@PathVariable int userId, @PathVariable long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);

            // Verify that the order belongs to the specified user
            if (order == null) {
                return ResponseEntity.notFound().build();
            }

            if (order.getUserId() != userId) {
                return ResponseEntity.status(403).body(null); // Forbidden - order doesn't belong to user
            }

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Get product items for a specific user order
     * @param userId User ID
     * @param orderId Order ID
     * @return List of product items for the specific user order
     */
    @GetMapping("/user/{userId}/order/{orderId}/items")
    public ResponseEntity<List<ProductItem>> getProductItemsForUserOrder(@PathVariable int userId, @PathVariable long orderId) {
        try {
            // First verify that the order belongs to the user
            Order order = orderService.getOrderById(orderId);

            if (order == null) {
                return ResponseEntity.notFound().build();
            }

            if (order.getUserId() != userId) {
                return ResponseEntity.status(403).body(null); // Forbidden - order doesn't belong to user
            }

            // Get product items for the order
            List<ProductItem> productItems = orderService.getProductItemsByOrderId(orderId);
            return ResponseEntity.ok(productItems);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ==================== Address Management Endpoints ====================

    @PutMapping("/{orderId}/address")
    public ResponseEntity<?> updateOrderAddress(@PathVariable long orderId,
                                          @RequestParam String recipientName,
                                          @RequestParam String phoneNumber,
                                          @RequestParam String houseAddress,
                                          @RequestParam String subDistrict,
                                          @RequestParam String district,
                                          @RequestParam String streetName,
                                          @RequestParam String province,
                                          @RequestParam String postalCode) {
        try {
            boolean success = orderService.updateOrderAddress(orderId, recipientName, phoneNumber,
                                                         houseAddress, subDistrict, district,
                                                         streetName, province, postalCode);
            if (success) {
                return ResponseEntity.ok("อัปเดตที่อยู่จัดส่งสำเร็จ");
            } else {
                return ResponseEntity.status(500).body("อัปเดตที่อยู่จัดส่งไม่สำเร็จ");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ข้อผิดพลาด: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    @PutMapping("/{orderId}/address/partial")
    public ResponseEntity<?> updatePartialOrderAddress(@PathVariable long orderId,
                                                 @RequestBody Map<String, String> addressUpdates) {
        try {
            String recipientName = addressUpdates.get("recipientName");
            String phoneNumber = addressUpdates.get("phoneNumber");
            String houseAddress = addressUpdates.get("houseAddress");
            String subDistrict = addressUpdates.get("subDistrict");
            String district = addressUpdates.get("district");
            String streetName = addressUpdates.get("streetName");
            String province = addressUpdates.get("province");
            String postalCode = addressUpdates.get("postalCode");

            boolean success = orderService.updatePartialOrderAddress(orderId, recipientName, phoneNumber,
                                                             houseAddress, subDistrict, district,
                                                             streetName, province, postalCode);
            if (success) {
                return ResponseEntity.ok("อัปเดตที่อยู่จัดส่งบางส่วนสำเร็จ");
            } else {
                return ResponseEntity.status(500).body("อัปเดตที่อยู่จัดส่งบางส่วนไม่สำเร็จ");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ข้อผิดพลาด: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }
}
