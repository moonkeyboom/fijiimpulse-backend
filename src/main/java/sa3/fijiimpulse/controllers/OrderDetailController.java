package sa3.fijiimpulse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.entity.OrderDetail;
import sa3.fijiimpulse.entity.ProductModel;
import sa3.fijiimpulse.service.OrderDetailService;
import sa3.fijiimpulse.service.OrderService;
import sa3.fijiimpulse.service.ProductItemService;
import sa3.fijiimpulse.service.ProductModelService;
import sa3.fijiimpulse.service.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final ProductModelService productModelService;

    public OrderDetailController(OrderDetailService orderDetailService, OrderService orderService, ProductModelService productModelService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.productModelService = productModelService;
    }

    @GetMapping
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailService.getAllOrderDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable Long id) {
        OrderDetail orderDetail = orderDetailService.getOrderDetailById(id);
        if (orderDetail != null) {
            return ResponseEntity.ok(orderDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/order/{orderId}")
    public List<OrderDetail> getOrderDetailsByOrderId(@PathVariable Long orderId) {
        return orderDetailService.getOrderDetailsByOrderId(orderId);
    }

    @GetMapping("/order/not-paid/{userId}")
    public Map<OrderDetail, String> getOrderDetailsInCart(@PathVariable int userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        long id = -1;
        for (Order o : orders) {
            if(o.getOrderStatus().equals(OrderStatus.WAITING_PAYMENT.getThaiTranslation())) {
                id = o.getOrderId();
            }
        }
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(id);
        Map<OrderDetail, String> result = new LinkedHashMap<>();
        for (OrderDetail detail : orderDetails) {
            ProductModel model = productModelService.getProductById(detail.getModelId());
            String modelName = model.getModelName();
            result.put(detail, modelName);
        }
        return result;
    }

    /**
     * Add quantity to order detail - Single endpoint for frontend
     *
     * Business Logic:
     * 1. Find user's order with WAITING_PAYMENT status
     * 2. If exists, add/update order detail for the model
     * 3. If not exists, create new order with WAITING_PAYMENT status and add order detail
     *
     * @param userId User ID
     * @param modelId Product model ID
     * @param quantity Quantity to add
     * @return Response with result message
     */
    //    ปรับจำนวนสินค้าในตะหร้า
    @PostMapping("/adjust-to-waiting-order")
    public ResponseEntity<?> adjustQuantityToWaitingOrder(
            @RequestParam int userId,
            @RequestParam int modelId,
            @RequestParam int quantity) {
        try {
            String result = orderDetailService.adjustQuantityToWaitingOrder(userId, modelId, quantity);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ข้อผิดพลาด: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    //เพิ่มสินค้าลงตะกร้า
    @PostMapping("/add-to-waiting-order")
    public ResponseEntity<?> addQuantityToWaitingOrder(
            @RequestParam int userId,
            @RequestParam int modelId,
            @RequestParam int quantity) {
        try {
            String result = orderDetailService.addQuantityToWaitingOrder(userId, modelId, quantity);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ข้อผิดพลาด: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrderDetail(@RequestBody OrderDetail orderDetail) {
        try {
            int result = orderDetailService.createOrderDetail(orderDetail);
            if (result > 0) {
                return ResponseEntity.ok("สร้าง OrderDetail สำเร็จ");
            } else {
                return ResponseEntity.status(500).body("สร้าง OrderDetail ไม่สำเร็จ");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    @PostMapping("/calculate-price")
    public ResponseEntity<?> calculateTotalPrice(@RequestParam Integer modelId, @RequestParam Integer quantity) {
        try {
            BigDecimal totalPrice = orderDetailService.calculateTotalPrice(modelId, quantity);
            return ResponseEntity.ok(totalPrice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ข้อผิดพลาด: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาดในการคำนวณราคา: " + e.getMessage());
        }
    }

    @PostMapping("/create-with-calculated-price")
    public ResponseEntity<?> createOrderDetailWithCalculatedPrice(@RequestBody OrderDetail orderDetail) {
        try {
            int result = orderDetailService.createOrderDetailWithCalculatedPrice(orderDetail);
            if (result > 0) {
                return ResponseEntity.ok("สร้าง OrderDetail พร้อมคำนวณราคาสำเร็จ");
            } else {
                return ResponseEntity.status(500).body("สร้าง OrderDetail ไม่สำเร็จ");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ข้อผิดพลาด: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable Long id, @RequestBody OrderDetail orderDetail) {
        try {
            orderDetail.setOrderDetailId(id);
            int result = orderDetailService.updateOrderDetail(orderDetail);
            if (result > 0) {
                return ResponseEntity.ok("อัปเดต OrderDetail สำเร็จ");
            } else {
                return ResponseEntity.status(500).body("อัปเดต OrderDetail ไม่สำเร็จ");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/update-with-calculated-price")
    public ResponseEntity<?> updateOrderDetailWithCalculatedPrice(@PathVariable Long id, @RequestBody OrderDetail orderDetail) {
        try {
            orderDetail.setOrderDetailId(id);
            int result = orderDetailService.updateOrderDetailWithCalculatedPrice(orderDetail);
            if (result > 0) {
                return ResponseEntity.ok("อัปเดต OrderDetail พร้อมคำนวณราคาสำเร็จ");
            } else {
                return ResponseEntity.status(500).body("อัปเดต OrderDetail ไม่สำเร็จ");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ข้อผิดพลาด: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable Long id) {
        try {
            int result = orderDetailService.deleteOrderDetail(id);
            if (result > 0) {
                return ResponseEntity.ok("ลบ OrderDetail สำเร็จ");
            } else {
                return ResponseEntity.status(500).body("ลบ OrderDetail ไม่สำเร็จ");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    @DeleteMapping("delete/order/{orderId}")
    public ResponseEntity<?> deleteOrderDetailsByOrderId(@PathVariable Long orderId) {
        try {
            int result = orderDetailService.deleteOrderDetailsByOrderId(orderId);
            if (result > 0) {
                return ResponseEntity.ok("ลบ OrderDetails สำหรับ Order ID " + orderId + " สำเร็จ");
            } else {
                return ResponseEntity.status(500).body("ลบ OrderDetails ไม่สำเร็จ");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    // ==================== Customer Quantity Management ====================

    @PutMapping("/{orderDetailId}/adjust-quantity")
    public ResponseEntity<?> adjustOrderDetailQuantity(@PathVariable Long orderDetailId,
                                             @RequestParam Integer newQuantity,
                                             @RequestParam int userId) {
        try {
            boolean success = orderDetailService.adjustOrderDetailQuantity(orderDetailId, newQuantity, userId);
            if (success) {
                return ResponseEntity.ok("ปรับจำนวนสินค้าสำเร็จ");
            } else {
                return ResponseEntity.status(500).body("ปรับจำนวนสินค้าไม่สำเร็จ");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ข้อผิดพลาด: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }

    @PostMapping("/create-new-order")
    public ResponseEntity<?> createNewOrderWithDetails(@RequestParam int userId,
                                                   @RequestBody List<OrderDetail> orderDetails,
                                                   @RequestParam String recipientName,
                                                   @RequestParam String phoneNumber,
                                                   @RequestParam String houseAddress,
                                                   @RequestParam String subDistrict,
                                                   @RequestParam String district,
                                                   @RequestParam String streetName,
                                                   @RequestParam String province,
                                                   @RequestParam String postalCode) {
        try {
            Long newOrderId = orderDetailService.createNewOrderWithDetails(
                userId, orderDetails, recipientName, phoneNumber,
                houseAddress, subDistrict, district, streetName, province, postalCode
            );
            return ResponseEntity.ok("สร้างคำสั่งซื้อใหม่สำเร็จ รหัสสินค้า: " + newOrderId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ข้อผิดพลาด: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("เกิดข้อผิดพลาด: " + e.getMessage());
        }
    }
}