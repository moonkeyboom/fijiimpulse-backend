package sa3.fijiimpulse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import sa3.fijiimpulse.dao.OrderDAO;
import sa3.fijiimpulse.dao.ProductItemDAO;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.service.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductItemService {

    @Autowired
    private ProductItemDAO productItemDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderService orderService; // ✅ ใช้เรียก service order

    // ------------------------ CRUD ------------------------
    public int save(ProductItem item) {
        return productItemDAO.save(item);
    }

    public ProductItem findBySerialNo(String serialNo) {
        return productItemDAO.findBySerialNo(serialNo);
    }

    public List<ProductItem> findAll() {
        return productItemDAO.findAll();
    }

    public int updateOrder(String serialNo, Long orderId) {
        return productItemDAO.updateOrder(serialNo, orderId);
    }

    public int delete(String serialNo) {
        return productItemDAO.delete(serialNo);
    }

    public List<ProductItem> getProductItemsByOrderId(long orderId) {
        return productItemDAO.findByOrderId(orderId);
    }

    // ------------------------ Core Logic ------------------------

    public void adjustProductItemsForOrder(int modelId, long orderId, int quantity) {
        // 1. ดึงรายการ ProductItem ปัจจุบันใน order
        List<ProductItem> currentItems = productItemDAO.findByOrderIdAndModelId(orderId, modelId);
        int currentCount = currentItems.size();

        // 2. ถ้ามากเกินไป → ถอดออก
        if (currentCount > quantity) {
            int toRemove = currentCount - quantity;
            for (int i = 0; i < toRemove; i++) {
                ProductItem item = currentItems.get(i);
                productItemDAO.clearOrderId(item.getSerialNo());
            }
            System.out.println("Removed " + toRemove + " items from order_id=" + orderId);
        }

        // 3. ถ้าน้อยเกินไป → เพิ่ม
        else if (currentCount < quantity) {
            int toAdd = quantity - currentCount;

            // ดึง ProductItem ที่ยังไม่ถูก assign
            List<ProductItem> availableItems = productItemDAO.findAvailableByModelId(modelId);
            int assigned = 0;

            for (ProductItem item : availableItems) {
                if (assigned >= toAdd) break;
                productItemDAO.updateOrder(item.getSerialNo(), orderId);
                assigned++;
            }

            int remaining = toAdd - assigned;

            // ถ้ายังไม่พอ → สร้างใหม่
            for (int i = 0; i < remaining; i++) {
                ProductItem newItem = new ProductItem();
                newItem.setSerialNo(UUID.randomUUID().toString());
                newItem.setModelId(modelId);
                newItem.setOrderId(orderId);
                productItemDAO.save(newItem);
            }

            System.out.println("Added " + toAdd + " items to order_id=" + orderId);
        }

        else {
            System.out.println("Order_id=" + orderId + " already has correct number of ProductItems.");
        }
    }

    private String safeGet(Map<String, String> map, String key) {
        String value = map.get(key);
        return (value == null || value.isBlank()) ? "" : value;
    }

//    ปุ่มเพิ่มสินค้าลงตระกร้า
    public void adjustProductItemsForUser(int modelId, int userId, int quantity, Map<String, String> address) {
        // 1. ดึง orders ของ user
        List<Order> userOrders = orderService.getOrdersByUserId(userId);
        System.out.println(address);

        // 2. หา order ที่ยังอยู่ในสถานะ PAYMENT_EVIDENCE_PENDING
        Order targetOrder = null;
        for (Order order : userOrders) {
            if (OrderStatus.PAYMENT_EVIDENCE_PENDING.getThaiTranslation()
                    .equals(order.getOrderStatus())) {
                targetOrder = order;
                break;
            }
        }

        // 3. ถ้ายังไม่มี → สร้าง order ใหม่
        if (targetOrder == null) {
            Order newOrder = new Order();
            newOrder.setUserId(userId);
            newOrder.setOrderStatus(OrderStatus.PAYMENT_EVIDENCE_PENDING.getThaiTranslation());
            newOrder.setRecipientName(safeGet(address, "name"));
            newOrder.setPhoneNumber(safeGet(address, "tel"));
            newOrder.setDistrict(safeGet(address, "district"));
            newOrder.setHouseAddress(safeGet(address, "houseAddress"));
            newOrder.setSubDistrict(safeGet(address, "subDistrict"));
            newOrder.setStreetName(safeGet(address, "street"));
            newOrder.setProvince(safeGet(address, "province"));
            newOrder.setPostalCode(safeGet(address, "postalCode"));
            newOrder.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));
            System.out.println(newOrder);
            int result = orderService.createOrder(newOrder);

            if (result > 0) {
                targetOrder = orderService.getOrderById(newOrder.getOrderId());
                System.out.println("Created new order for userId=" + userId);
            } else {
                throw new RuntimeException("ไม่สามารถสร้าง Order ใหม่ให้ userId=" + userId);
            }
        }

        // 4. เรียก adjustProductItemsForOrder สำหรับ order ที่เจอ/สร้าง
        adjustProductItemsForOrder(modelId, targetOrder.getOrderId(), quantity);
    }

    // สำหรับสร้าง ProductItem ใหม่ที่ยังไม่ถูก assign
    public void addProductItem(int modelId) {
        ProductItem newItem = new ProductItem();
        newItem.setSerialNo(UUID.randomUUID().toString());
        newItem.setModelId(modelId);
        newItem.setOrderId(null);
        productItemDAO.save(newItem);
    }

    // ✅ ดึง ProductItem ทั้งหมดของ User (รวมทุก Order)
    public List<ProductItem> getProductItemsByUserId(int userId) {
        // 1️⃣ ดึงออเดอร์ทั้งหมดของ user
        List<Order> userOrders = orderDAO.findByUserId(userId);

        // 2️⃣ รวม ProductItem ของทุก order
        List<ProductItem> allItems = new ArrayList<>();
        for (Order order : userOrders) {
            List<ProductItem> items = productItemDAO.findByOrderId(order.getOrderId());
            allItems.addAll(items);
        }

        return allItems;
    }

    public List<Map<String, Object>> getProductListFromOrderId(int orderId) {
        return orderDAO.findItemsListFromOrderId(orderId);
    }
}
