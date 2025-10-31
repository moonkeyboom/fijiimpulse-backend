package sa3.fijiimpulse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.OrderDAO;
import sa3.fijiimpulse.dao.ProductItemDAO;
import sa3.fijiimpulse.dao.OrderDetailDAO;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.entity.OrderDetail;
import sa3.fijiimpulse.service.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ProductItemService {

    @Autowired
    private ProductItemDAO productItemDAO;

    @Autowired
    private OrderDAO orderDAO;

    // Removed circular dependency - OrderService will call ProductItemService instead

    @Autowired
    private OrderDetailDAO orderDetailDAO;

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

    // ==================== Payment Approval Workflow ====================

    /**
     * Generate and assign ProductItems to Order after payment approval
     * Creates ProductItems based on OrderDetail quantities
     */
    public List<ProductItem> generateProductItemsAfterPaymentApproval(long orderId) {
        try {
            // Validate order status is PAYMENT_CONFIRMED
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Order not found with ID: " + orderId);
            }

            if (!OrderStatus.PAYMENT_CONFIRMED.getThaiTranslation().equals(order.getOrderStatus())) {
                throw new IllegalArgumentException("Cannot generate ProductItems. Order status is: " + order.getOrderStatus());
            }

            // Get OrderDetails for the order
            List<OrderDetail> orderDetails = orderDetailDAO.findByOrderId(orderId);
            if (orderDetails == null || orderDetails.isEmpty()) {
                throw new IllegalArgumentException("No order details found for Order ID: " + orderId);
            }

            List<ProductItem> createdItems = new ArrayList<>();

            for (OrderDetail orderDetail : orderDetails) {
                // Check available ProductItems for this model (not assigned to any order)
                List<ProductItem> availableItems = getAvailableProductItemsByModel(orderDetail.getModelId());

                int requiredQuantity = orderDetail.getOrderQuantity();

                if (availableItems.size() < requiredQuantity) {
                    // Not enough available items, create new ones
                    int itemsToCreate = requiredQuantity - availableItems.size();

                    for (int i = 0; i < itemsToCreate; i++) {
                        ProductItem newItem = createNewProductItem(orderDetail.getModelId());
                        availableItems.add(newItem);
                    }
                }

                // Assign items to order
                for (int i = 0; i < requiredQuantity && i < availableItems.size(); i++) {
                    ProductItem item = availableItems.get(i);
                    item.setOrderId(orderId);
                    productItemDAO.updateOrder(item.getSerialNo(), orderId);
                    createdItems.add(item);
                }
            }

            return createdItems;
        } catch (Exception e) {
            throw new RuntimeException("Error generating ProductItems after payment approval: " + e.getMessage(), e);
        }
    }

    /**
     * Get available ProductItems for a specific model (not assigned to any order)
     */
    private List<ProductItem> getAvailableProductItemsByModel(int modelId) {
        return productItemDAO.findAvailableByModelId(modelId);
    }

    /**
     * Create a new ProductItem with auto-generated serial number
     */
    private ProductItem createNewProductItem(int modelId) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String serialNo = String.format("SOAP-%s-%s", timestamp, randomSuffix);

        ProductItem newItem = new ProductItem();
        newItem.setSerialNo(serialNo);
        newItem.setModelId(modelId);
        newItem.setOrderId(null); // Available, not assigned

        productItemDAO.save(newItem);
        return newItem;
    }

    /**
     * Check if enough ProductItems are available for an order
     */
    public Map<String, Object> checkProductItemAvailability(long orderId) {
        try {
            List<OrderDetail> orderDetails = orderDetailDAO.findByOrderId(orderId);
            Map<String, Object> result = new java.util.HashMap<>();

            boolean isAvailable = true;
            List<Map<String, Object>> availabilityInfo = new ArrayList<>();

            for (OrderDetail detail : orderDetails) {
                List<ProductItem> availableItems = getAvailableProductItemsByModel(detail.getModelId());
                int requiredQuantity = detail.getOrderQuantity();
                int availableQuantity = availableItems.size();
                int shortage = Math.max(0, requiredQuantity - availableQuantity);

                Map<String, Object> info = new java.util.HashMap<>();
                info.put("modelId", detail.getModelId());
                info.put("requiredQuantity", requiredQuantity);
                info.put("availableQuantity", availableQuantity);
                info.put("shortage", shortage);
                info.put("isAvailable", shortage == 0);

                availabilityInfo.add(info);

                if (shortage > 0) {
                    isAvailable = false;
                }
            }

            result.put("isAvailable", isAvailable);
            result.put("orderDetails", availabilityInfo);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error checking ProductItem availability: " + e.getMessage(), e);
        }
    }

    // ==================== Cart Management Methods ====================

    /**
     * Adjust ProductItems for a specific order
     * Used when customer adjusts quantity in existing order
     */
    public void adjustProductItemsForOrder(int modelId, long orderId, int quantity) {
        try {
            // Get current ProductItems for this order and model
            List<ProductItem> currentItems = productItemDAO.findByOrderIdAndModelId(orderId, modelId);
            int currentQuantity = currentItems.size();

            if (quantity == currentQuantity) {
                return; // No adjustment needed
            }

            if (quantity > currentQuantity) {
                // Need to add more items
                int itemsToAdd = quantity - currentQuantity;
                for (int i = 0; i < itemsToAdd; i++) {
                    addProductItemAndAssign(modelId, orderId);
                }
            } else {
                // Need to remove items
                int itemsToRemove = currentQuantity - quantity;
                for (int i = 0; i < itemsToRemove; i++) {
                    if (!currentItems.isEmpty()) {
                        ProductItem itemToRemove = currentItems.get(0);
                        clearOrderAssignment(itemToRemove.getSerialNo());
                        currentItems.remove(0);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adjusting ProductItems for order: " + e.getMessage(), e);
        }
    }

    /**
     * Adjust ProductItems for a user (creates new order if needed)
     * Used for cart operations - manages user's shopping cart
     */
    public void adjustProductItemsForUser(int modelId, int userId, int quantity, Map<String, String> address) {
        try {
            // Find existing orders with WAITING_PAYMENT status
            List<Order> pendingOrders = findPendingOrdersByUserId(userId);
            Order targetOrder = null;

            // Find existing order with this model or create new one
            for (Order order : pendingOrders) {
                List<OrderDetail> details = orderDetailDAO.findByOrderId(order.getOrderId());
                boolean hasThisModel = details.stream().anyMatch(d -> d.getModelId() == modelId);
                if (hasThisModel) {
                    targetOrder = order;
                    break;
                }
            }

            if (targetOrder == null && quantity > 0) {
                // Create new order for this user
                targetOrder = createNewOrderForUser(userId, address);
            }

            if (targetOrder != null) {
                adjustProductItemsForOrder(modelId, targetOrder.getOrderId(), quantity);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adjusting ProductItems for user: " + e.getMessage(), e);
        }
    }

    // Helper methods

    /**
     * Add ProductItem and assign to order
     */
    private void addProductItemAndAssign(int modelId, long orderId) {
        ProductItem newItem = createNewProductItem(modelId);
        newItem.setOrderId(orderId);
        productItemDAO.updateOrder(newItem.getSerialNo(), orderId);
    }

    /**
     * Clear order assignment from ProductItem
     */
    private void clearOrderAssignment(String serialNo) {
        productItemDAO.clearOrderId(serialNo);
    }

    /**
     * Find pending orders for a user
     */
    private List<Order> findPendingOrdersByUserId(int userId) {
        return orderDAO.findByUserId(userId).stream()
                .filter(order -> OrderStatus.WAITING_PAYMENT.getThaiTranslation().equals(order.getOrderStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Create new order for user with address
     */
    private Order createNewOrderForUser(int userId, Map<String, String> address) {
        Order newOrder = new Order();
        newOrder.setUserId(userId);
        newOrder.setOrderStatus(OrderStatus.WAITING_PAYMENT.getThaiTranslation());
        newOrder.setRecipientName(address.get("recipientName"));
        newOrder.setPhoneNumber(address.get("phoneNumber"));
        newOrder.setHouseAddress(address.get("houseAddress"));
        newOrder.setSubDistrict(address.get("subDistrict"));
        newOrder.setDistrict(address.get("district"));
        newOrder.setStreetName(address.get("streetName"));
        newOrder.setProvince(address.get("province"));
        newOrder.setPostalCode(address.get("postalCode"));
        newOrder.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));
        newOrder.setGrandTotalPrice(java.math.BigDecimal.ZERO); // Will be updated later

        orderDAO.insert(newOrder);
        return newOrder;
    }
}
