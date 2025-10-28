package sa3.fijiimpulse.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.OrderDAO;
import sa3.fijiimpulse.dao.PaymentDAO;
import sa3.fijiimpulse.dao.ProductItemDAO;
import sa3.fijiimpulse.dao.ProductModelDAO;
import sa3.fijiimpulse.dao.RecipeMaterialDAO;
import sa3.fijiimpulse.dao.MaterialDAO;
import sa3.fijiimpulse.dao.OrderDetailDAO;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.entity.ProductModel;
import sa3.fijiimpulse.entity.RecipeMaterial;
import sa3.fijiimpulse.entity.Material;
import sa3.fijiimpulse.entity.OrderDetail;
import sa3.fijiimpulse.service.enums.OrderStatus;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderService {
    private final OrderDAO orderDAO;
    private final PaymentDAO paymentDAO;
    private final ProductItemDAO productItemDAO;
    private final ProductModelDAO productModelDAO;
    private final RecipeMaterialDAO recipeMaterialDAO;
    private final MaterialDAO materialDAO;
    private final OrderDetailDAO orderDetailDAO;
    private final JdbcTemplate jdbcTemplate;

    public OrderService(OrderDAO orderDAO, PaymentDAO paymentDAO,
                        ProductItemDAO productItemDAO, ProductModelDAO productModelDAO,
                        RecipeMaterialDAO recipeMaterialDAO, MaterialDAO materialDAO,
                        OrderDetailDAO orderDetailDAO, JdbcTemplate jdbcTemplate) {
        this.orderDAO = orderDAO;
        this.paymentDAO = paymentDAO;
        this.productItemDAO = productItemDAO;
        this.productModelDAO = productModelDAO;
        this.recipeMaterialDAO = recipeMaterialDAO;
        this.materialDAO = materialDAO;
        this.orderDetailDAO = orderDetailDAO;
        this.jdbcTemplate = jdbcTemplate;
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
            order.setOrderStatus(OrderStatus.WAITING_PAYMENT.getThaiTranslation());
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

    /**
     * Get all product items for a specific user
     * @param userId User ID
     * @return List of all product items belonging to the user
     */
    public List<ProductItem> getProductItemsByUserId(int userId) {
        // Get all orders for the user
        List<Order> userOrders = orderDAO.findByUserId(userId);

        // Collect all product items from all user's orders
        List<ProductItem> allUserProductItems = new ArrayList<>();

        for (Order order : userOrders) {
            List<ProductItem> productItems = productItemDAO.findByOrderId(order.getOrderId());
            allUserProductItems.addAll(productItems);
        }

        return allUserProductItems;
    }

    /**
     * Get all order details for a specific user
     * @param userId User ID
     * @return List of all order details belonging to the user
     */
    public List<OrderDetail> getOrderDetailsByUserId(int userId) {
        // Get all orders for the user
        List<Order> userOrders = orderDAO.findByUserId(userId);

        // Collect all order details from all user's orders
        List<OrderDetail> allUserOrderDetails = new ArrayList<>();

        for (Order order : userOrders) {
            List<OrderDetail> orderDetails = orderDetailDAO.findByOrderId(order.getOrderId());
            allUserOrderDetails.addAll(orderDetails);
        }

        return allUserOrderDetails;
    }

    // ==================== Use Case 2M: ตรวจสอบและยืนยันหลักฐานการชำระเงิน ====================

    public List<Order> getPendingPaymentOrders() {
//        return orderDAO.findByOrderStatus("รอตรวจสอบหลักฐานการชำระเงิน");_
        return orderDAO.findByOrderStatus(OrderStatus.WAITING_VERIFICATION.getThaiTranslation());
    }

    public Payment getPaymentByOrderId(long orderId) {
        return paymentDAO.findByOrderId(orderId);
    }

    public boolean approvePayment(long orderId) {
        try {
            // Check ProductItem availability before approving
            Map<String, Object> availability = checkProductItemAvailabilityForOrder(orderId);
            if (!(Boolean) availability.get("isAvailable")) {
                // Not enough ProductItems available, create missing ones automatically
                createMissingProductItemsForOrder(orderId, availability);
                System.out.println("Created missing ProductItems for order " + orderId + " due to insufficient stock");
            }

            // Update order status to PAYMENT_CONFIRMED
            int result = orderDAO.updateOrderStatus(orderId, OrderStatus.PAYMENT_CONFIRMED.getThaiTranslation());

            if (result > 0) {
                // Generate ProductItems for the order based on OrderDetail quantities
                List<ProductItem> createdItems = generateProductItemsAfterPaymentApproval(orderId);
                System.out.println("Assigned " + createdItems.size() + " ProductItems for order " + orderId);
            }

            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error approving payment: " + e.getMessage(), e);
        }
    }

    public boolean rejectPayment(long orderId) {
//        int result = orderDAO.updateOrderStatus(orderId, "หลักฐานการชำระเงินถูกปฏิเสธ รอชำระเงินและอัปโหลดหลักฐานการชำระเงินอีกครั้ง");
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.PAYMENT_REJECTED.getThaiTranslation());
        return result > 0;
    }

    // ==================== Use Case: Production & Shipping Flow ====================

    /**
     * UC: ทำเครื่องหมายว่าผลิตสินค้าเสร็จแล้ว
     */
    public boolean markAsProduced(long orderId) {
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.PRODUCTION_COMPLETE.getThaiTranslation());
        return result > 0;
    }

    /**
     * UC: เตรียมจัดส่งสินค้า
     */
    public boolean prepareShipping(long orderId) {
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.WAITING_SHIPMENT.getThaiTranslation());
        return result > 0;
    }

    /**
     * UC-Shipping: จัดส่งสินค้า
     */
    public boolean shipOrder(long orderId) {
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.SHIPPEDSHIPPING_IN_PROGRESS.getThaiTranslation());
        return result > 0;
    }

    /**
     * UC-Delivery: บันทึกการจัดส่งสำเร็จ
     */
    public boolean markAsDelivered(long orderId) {
        int result = orderDAO.updateOrderStatus(orderId, OrderStatus.SHIPPED.getThaiTranslation());
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

    // ==================== Address Management ====================

    /**
     * Update shipping address for an order
     * Only allows address updates when order status is WAITING_PAYMENT
     */
    public boolean updateOrderAddress(long orderId, String recipientName, String phoneNumber,
                                String houseAddress, String subDistrict, String district,
                                String streetName, String province, String postalCode) {
        try {
            // Check if order exists and get current status
            Order currentOrder = orderDAO.findById(orderId);
            if (currentOrder == null) {
                throw new IllegalArgumentException("Order not found with ID: " + orderId);
            }

            // Validate that order can be modified (only in WAITING_PAYMENT status)
            if (!OrderStatus.WAITING_PAYMENT.getThaiTranslation().equals(currentOrder.getOrderStatus())) {
                throw new IllegalArgumentException("Cannot update address. Order status is: " + currentOrder.getOrderStatus());
            }

            // Validate address fields
            validateAddress(recipientName, phoneNumber, houseAddress, subDistrict, district, province, postalCode);

            // Update address information
            String sql = "UPDATE ORDERS SET Recipient_name=?, Phone_number=?, House_address=?, " +
                       "Sub_district=?, District=?, Street_name=?, Province=?, Postal_code=? WHERE Order_id=?";

            int result = jdbcTemplate.update(sql, recipientName, phoneNumber, houseAddress,
                                       subDistrict, district, streetName, province, postalCode, orderId);

            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error updating order address: " + e.getMessage(), e);
        }
    }

    /**
     * Update partial address information (only provided fields)
     */
    public boolean updatePartialOrderAddress(long orderId, String recipientName, String phoneNumber,
                                       String houseAddress, String subDistrict, String district,
                                       String streetName, String province, String postalCode) {
        try {
            // Check if order exists and get current status
            Order currentOrder = orderDAO.findById(orderId);
            if (currentOrder == null) {
                throw new IllegalArgumentException("Order not found with ID: " + orderId);
            }

            // Validate that order can be modified
            if (!OrderStatus.WAITING_PAYMENT.getThaiTranslation().equals(currentOrder.getOrderStatus())) {
                throw new IllegalArgumentException("Cannot update address. Order status is: " + currentOrder.getOrderStatus());
            }

            // Build dynamic update query
            StringBuilder sql = new StringBuilder("UPDATE ORDERS SET ");
            List<Object> params = new ArrayList<>();

            if (recipientName != null && !recipientName.trim().isEmpty()) {
                sql.append("Recipient_name=?, ");
                params.add(recipientName);
            }
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                sql.append("Phone_number=?, ");
                params.add(phoneNumber);
            }
            if (houseAddress != null && !houseAddress.trim().isEmpty()) {
                sql.append("House_address=?, ");
                params.add(houseAddress);
            }
            if (subDistrict != null && !subDistrict.trim().isEmpty()) {
                sql.append("Sub_district=?, ");
                params.add(subDistrict);
            }
            if (district != null && !district.trim().isEmpty()) {
                sql.append("District=?, ");
                params.add(district);
            }
            if (streetName != null && !streetName.trim().isEmpty()) {
                sql.append("Street_name=?, ");
                params.add(streetName);
            }
            if (province != null && !province.trim().isEmpty()) {
                sql.append("Province=?, ");
                params.add(province);
            }
            if (postalCode != null && !postalCode.trim().isEmpty()) {
                sql.append("Postal_code=?, ");
                params.add(postalCode);
            }

            // Remove trailing comma and add WHERE clause
            if (params.isEmpty()) {
                throw new IllegalArgumentException("No address fields provided for update");
            }

            sql.setLength(sql.length() - 2); // Remove trailing comma
            sql.append(" WHERE Order_id=?");
            params.add(orderId);

            int result = jdbcTemplate.update(sql.toString(), params.toArray());
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error updating partial order address: " + e.getMessage(), e);
        }
    }

    /**
     * Validate address fields
     */
    private void validateAddress(String recipientName, String phoneNumber, String houseAddress,
                            String subDistrict, String district, String province, String postalCode) {
        if (recipientName == null || recipientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient name is required");
        }
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (houseAddress == null || houseAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("House address is required");
        }
        if (district == null || district.trim().isEmpty()) {
            throw new IllegalArgumentException("District is required");
        }
        if (province == null || province.trim().isEmpty()) {
            throw new IllegalArgumentException("Province is required");
        }
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Postal code is required");
        }

        // Basic phone number validation (Thai format)
        if (!phoneNumber.matches("^[0-9]{9,10}$") && !phoneNumber.matches("^0[0-9]{9}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        // Basic postal code validation (Thai format)
        if (!postalCode.matches("^[0-9]{5}$")) {
            throw new IllegalArgumentException("Invalid postal code format. Must be 5 digits");
        }
    }

    // ==================== ProductItem Management ====================

    /**
     * Check if enough ProductItems are available for an order
     */
    public Map<String, Object> checkProductItemAvailabilityForOrder(long orderId) {
        try {
            List<OrderDetail> orderDetails = orderDetailDAO.findByOrderId(orderId);
            Map<String, Object> result = new HashMap<>();

            boolean isAvailable = true;
            List<Map<String, Object>> availabilityInfo = new ArrayList<>();

            for (OrderDetail detail : orderDetails) {
                List<ProductItem> availableItems = getAvailableProductItemsByModel(detail.getModelId());
                int requiredQuantity = detail.getOrderQuantity();
                int availableQuantity = availableItems.size();
                int shortage = Math.max(0, requiredQuantity - availableQuantity);

                Map<String, Object> info = new HashMap<>();
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

    /**
     * Create missing ProductItems for an order when there's insufficient stock
     * This method is called during payment approval to automatically create needed items
     */
    private void createMissingProductItemsForOrder(long orderId, Map<String, Object> availability) {
        try {
            List<Map<String, Object>> orderDetails = (List<Map<String, Object>>) availability.get("orderDetails");

            for (Map<String, Object> detail : orderDetails) {
                int shortage = (Integer) detail.get("shortage");
                int modelId = (Integer) detail.get("modelId");

                if (shortage > 0) {
                    // Create missing ProductItems for this model
                    for (int i = 0; i < shortage; i++) {
                        ProductItem newItem = new ProductItem();
                        newItem.setModelId(modelId);
                        newItem.setOrderId(null); // Will be assigned later
                        newItem.setSerialNo(generateSerialNumber(modelId));

                        productItemDAO.save(newItem);
                        System.out.println("Created new ProductItem for model " + modelId + " with serial " + newItem.getSerialNo());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating missing ProductItems: " + e.getMessage(), e);
        }
    }

    /**
     * Generate a unique serial number for a ProductItem
     */
    private String generateSerialNumber(int modelId) {
        // Generate serial number with format: SN{modelId}{timestamp}{random}
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return "SN" + modelId + timestamp + random;
    }

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
}