package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.OrderDAO;
import sa3.fijiimpulse.dao.PaymentDAO;
import sa3.fijiimpulse.dao.ProductItemDAO;
import sa3.fijiimpulse.dao.ProductModelDAO;
import sa3.fijiimpulse.dao.RecipeMaterialDAO;
import sa3.fijiimpulse.dao.MaterialDAO;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.entity.ProductModel;
import sa3.fijiimpulse.entity.RecipeMaterial;
import sa3.fijiimpulse.entity.Material;
import sa3.fijiimpulse.service.enums.OrderStatus;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class OrderService {
    private final OrderDAO orderDAO;
    private final PaymentDAO paymentDAO;
    private final ProductItemDAO productItemDAO;
    private final ProductModelDAO productModelDAO;
    private final RecipeMaterialDAO recipeMaterialDAO;
    private final MaterialDAO materialDAO;

    public OrderService(OrderDAO orderDAO, PaymentDAO paymentDAO,
                       ProductItemDAO productItemDAO, ProductModelDAO productModelDAO,
                       RecipeMaterialDAO recipeMaterialDAO, MaterialDAO materialDAO) {
        this.orderDAO = orderDAO;
        this.paymentDAO = paymentDAO;
        this.productItemDAO = productItemDAO;
        this.productModelDAO = productModelDAO;
        this.recipeMaterialDAO = recipeMaterialDAO;
        this.materialDAO = materialDAO;
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public Order getOrderById(long id) {
        return orderDAO.findById(id);
    }

    public int createOrder(Order order) {
        // Set initial status เป็น "รอชำระเงินและอัปโหลดหลักฐานการชำระเงิน"
        if (order.getOrderStatus() == null || order.getOrderStatus().isEmpty()) {
            order.setOrderStatus(OrderStatus.PAYMENT_EVIDENCE_PENDING.getThaiTranslation());
        }

        // Set orderDate เป็นเวลาปัจจุบัน
        if (order.getOrderDate() == null) {
            order.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));
        }

        return orderDAO.insert(order);
    }

    public int updateOrder(Order order) {
        return orderDAO.update(order);
    }

    public int deleteOrder(long id) {
        return orderDAO.delete(id);
    }

    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.findByUserId(userId);
    }

    // ==================== Use Case 2M: ตรวจสอบและยืนยันหลักฐานการชำระเงิน ====================

    public List<Order> getPendingPaymentOrders() {
//        return orderDAO.findByOrderStatus("รอตรวจสอบหลักฐานการชำระเงิน");_
        return orderDAO.findByOrderStatus(OrderStatus.PENDING_RECEIPT_VERIFICATION.getThaiTranslation());
    }

    public Payment getPaymentByOrderId(long orderId) {
        return paymentDAO.findByOrderId(orderId);
    }

    public boolean approvePayment(long orderId) {
//        int result = orderDAO.updateOrderStatus(orderId, "ได้รับการยืนยันการชำระเงิน");
        int result = orderDAO.updateOrderStatus(orderId,OrderStatus.PAYMENT_APPROVED.getThaiTranslation());
        return result > 0;
    }

    public boolean rejectPayment(long orderId) {
//        int result = orderDAO.updateOrderStatus(orderId, "หลักฐานการชำระเงินถูกปฏิเสธ รอชำระเงินและอัปโหลดหลักฐานการชำระเงินอีกครั้ง");
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.PAYMENT_EVIDENCE_PENDING_AGAIN.getThaiTranslation());
        return result > 0;
    }

    // ==================== Use Case: Production & Shipping Flow ====================

    /**
     * UC: ทำเครื่องหมายว่าผลิตสินค้าเสร็จแล้ว
     */
    public boolean markAsProduced(long orderId) {
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.PRODUCED.getThaiTranslation());
        return result > 0;
    }

    /**
     * UC: เตรียมจัดส่งสินค้า
     */
    public boolean prepareShipping(long orderId) {
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.READY_TO_SHIP.getThaiTranslation());
        return result > 0;
    }

    /**
     * UC-Shipping: จัดส่งสินค้า
     */
    public boolean shipOrder(long orderId) {
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.SHIPPED.getThaiTranslation());
        return result > 0;
    }

    /**
     * UC-Delivery: บันทึกการจัดส่งสำเร็จ
     */
    public boolean markAsDelivered(long orderId) {
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.DELIVERED.getThaiTranslation());
        return result > 0;
    }

    // ==================== Use Case 3M: ตรวจสอบทรัพยากร ====================


    public Map<String, Object> checkMaterialsForOrder(long orderId) {
        Map<String, Object> result = new HashMap<>();

        List<ProductItem> productItems = productItemDAO.findByOrderId(orderId);

        Map<Integer, Integer> requiredMaterialsMap = new HashMap<>();

        for (ProductItem item : productItems) {
            // หา Model ของ Product นี้
            ProductModel model = productModelDAO.findById(item.getModelId());

            // หา Recipe Materials ของ Model นี้
            List<RecipeMaterial> recipeMaterials = recipeMaterialDAO.findByRecipeId(model.getRecipeId());

            // รวมจำนวนวัสดุที่ต้องใช้
            for (RecipeMaterial rm : recipeMaterials) {
                int currentQty = requiredMaterialsMap.getOrDefault(rm.getMaterialId(), 0);
                requiredMaterialsMap.put(rm.getMaterialId(), currentQty + rm.getRequiredQuantity());
            }
        }

        // เช็ค Stock ปัจจุบัน และหาวัสดุที่ไม่เพียงพอ
        List<Map<String, Object>> requiredMaterials = new ArrayList<>();
        List<Map<String, Object>> insufficientMaterials = new ArrayList<>();
        boolean isSufficient = true;

        for (Map.Entry<Integer, Integer> entry : requiredMaterialsMap.entrySet()) {
            int materialId = entry.getKey();
            int requiredQty = entry.getValue();

            Material material = materialDAO.findById(materialId);
            int stockQty = material.getStockQuantity();

            Map<String, Object> materialInfo = new HashMap<>();
            materialInfo.put("materialId", materialId);
            materialInfo.put("materialName", material.getMaterialName());
            materialInfo.put("requiredQuantity", requiredQty);
            materialInfo.put("stockQuantity", stockQty);

            requiredMaterials.add(materialInfo);

            // ถ้า Stock ไม่พอ
            if (stockQty < requiredQty) {
                isSufficient = false;
                materialInfo.put("shortage", requiredQty - stockQty);
                insufficientMaterials.add(materialInfo);
            }
        }

        result.put("isSufficient", isSufficient);
        result.put("requiredMaterials", requiredMaterials);
        result.put("insufficientMaterials", insufficientMaterials);

        return result;
    }


    // ดึง ProductItem ทั้งหมดของ Order
    public List<ProductItem> getProductItemsByOrderId(long orderId) {
        return productItemDAO.findByOrderId(orderId);
    }
}
