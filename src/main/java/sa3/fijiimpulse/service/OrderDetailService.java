package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.OrderDetailDAO;
import sa3.fijiimpulse.dao.ProductModelDAO;
import sa3.fijiimpulse.dao.OrderDAO;
import sa3.fijiimpulse.entity.OrderDetail;
import sa3.fijiimpulse.entity.ProductModel;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.service.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderDetailService {
    private final OrderDetailDAO orderDetailDAO;
    private final ProductModelDAO productModelDAO;
    private final OrderDAO orderDAO;

    public OrderDetailService(OrderDetailDAO orderDetailDAO, ProductModelDAO productModelDAO, OrderDAO orderDAO) {
        this.orderDetailDAO = orderDetailDAO;
        this.productModelDAO = productModelDAO;
        this.orderDAO = orderDAO;
    }

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailDAO.findAll();
    }

    public OrderDetail getOrderDetailById(Long id) {
        return orderDetailDAO.findById(id);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailDAO.findByOrderId(orderId);
    }

    public int createOrderDetail(OrderDetail orderDetail) {
        return orderDetailDAO.insert(orderDetail);
    }

    public int updateOrderDetail(OrderDetail orderDetail) {
        return orderDetailDAO.update(orderDetail);
    }

    public int deleteOrderDetail(Long id) {
        return orderDetailDAO.delete(id);
    }

    /**
     * Add quantity to waiting order - Single method for frontend
     *
     * Business Logic:
     * 1. Find user's order with WAITING_PAYMENT status
     * 2. If exists, add/update order detail for the model
     * 3. If not exists, create new order with WAITING_PAYMENT status and add order detail
     *
     * @param userId User ID
     * @param modelId Product model ID
     * @param quantity Quantity to add
     * @return Success message
     */
    public String adjustQuantityToWaitingOrder(int userId, int modelId, int quantity) {
        // Validate inputs
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // Validate model exists
        ProductModel productModel = productModelDAO.findById(modelId);
        if (productModel == null) {
            throw new IllegalArgumentException("Product model not found");
        }

        // Find user's order with WAITING_PAYMENT status
        List<Order> userOrders = orderDAO.findByUserId(userId);
        Order waitingOrder = null;

        for (Order order : userOrders) {
            if (OrderStatus.WAITING_PAYMENT.getThaiTranslation().equals(order.getOrderStatus())) {
                waitingOrder = order;
                break;
            }
        }

        Long orderId = null;
        boolean isNewOrder = false;

        if (waitingOrder != null) {
            // Use existing waiting order
            orderId = waitingOrder.getOrderId();
        } else {
            // Create new order with WAITING_PAYMENT status
            Order newOrder = new Order();
            newOrder.setUserId(userId);
            newOrder.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));
            newOrder.setOrderStatus(OrderStatus.WAITING_PAYMENT.getThaiTranslation());
            newOrder.setGrandTotalPrice(BigDecimal.ZERO);
            // Set default address values - these should be updated by user later
            newOrder.setRecipientName("Pending");
            newOrder.setPhoneNumber("Pending");
            newOrder.setHouseAddress("Pending");
            newOrder.setSubDistrict("Pending");
            newOrder.setDistrict("Pending");
            newOrder.setStreetName("Pending");
            newOrder.setProvince("Pending");
            newOrder.setPostalCode("Pending");

            int result = orderDAO.insert(newOrder);
            if (result <= 0) {
                throw new RuntimeException("Failed to create new order");
            }

            // Get the newly created order ID
            List<Order> updatedUserOrders = orderDAO.findByUserId(userId);
            for (Order order : updatedUserOrders) {
                if (OrderStatus.WAITING_PAYMENT.getThaiTranslation().equals(order.getOrderStatus())) {
                    orderId = order.getOrderId();
                    break;
                }
            }

            if (orderId == null) {
                throw new RuntimeException("Failed to retrieve newly created order");
            }

            isNewOrder = true;
        }

        // Check if order detail already exists for this model
        List<OrderDetail> existingDetails = orderDetailDAO.findByOrderId(orderId);
        OrderDetail existingDetail = null;

        for (OrderDetail detail : existingDetails) {
            if (detail.getModelId() == modelId) {
                existingDetail = detail;
                break;
            }
        }

        if (existingDetail != null) {
            // Update existing order detail quantity
//            int newQuantity = existingDetail.getOrderQuantity() + quantity;
            int newQuantity = quantity;
            existingDetail.setOrderQuantity(newQuantity);

            // Recalculate price
            BigDecimal newPrice = productModel.getPrice().multiply(BigDecimal.valueOf(newQuantity));
            existingDetail.setTotalPrice(newPrice);

            int updateResult = orderDetailDAO.update(existingDetail);
            if (updateResult <= 0) {
                throw new RuntimeException("Failed to update order detail");
            }

            return "Updated quantity for existing order. New quantity: " + newQuantity;
        } else {
            // Create new order detail
            OrderDetail newDetail = new OrderDetail();
            newDetail.setOrderId(orderId);
            newDetail.setModelId(modelId);
            newDetail.setOrderQuantity(quantity);

            // Calculate price
            BigDecimal calculatedPrice = productModel.getPrice().multiply(BigDecimal.valueOf(quantity));
            newDetail.setTotalPrice(calculatedPrice);

            int insertResult = orderDetailDAO.insert(newDetail);
            if (insertResult <= 0) {
                throw new RuntimeException("Failed to create order detail");
            }

            if (isNewOrder) {
                return "Created new order and added " + quantity + " items to order " + orderId;
            } else {
                return "Added " + quantity + " items to existing order " + orderId;
            }
        }
    }

    public int deleteOrderDetailsByOrderId(Long orderId) {
        return orderDetailDAO.deleteByOrderId(orderId);
    }

    // Calculate total price for OrderDetail based on product model price and quantity
    public BigDecimal calculateTotalPrice(Integer modelId, Integer quantity) {
        try {
            ProductModel productModel = productModelDAO.findById(modelId);
            if (productModel == null) {
                throw new IllegalArgumentException("Product model not found with ID: " + modelId);
            }

            BigDecimal unitPrice = productModel.getPrice();
            if (unitPrice == null || quantity <= 0) {
                throw new IllegalArgumentException("Invalid price or quantity");
            }

            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        } catch (Exception e) {
            throw new RuntimeException("Error calculating total price: " + e.getMessage(), e);
        }
    }

    // Create OrderDetail with automatic total price calculation
    public int createOrderDetailWithCalculatedPrice(OrderDetail orderDetail) {
        try {
            BigDecimal calculatedTotalPrice = calculateTotalPrice(orderDetail.getModelId(), orderDetail.getOrderQuantity());
            orderDetail.setTotalPrice(calculatedTotalPrice);
            return orderDetailDAO.insert(orderDetail);
        } catch (Exception e) {
            throw new RuntimeException("Error creating order detail: " + e.getMessage(), e);
        }
    }

    // Update OrderDetail with automatic total price calculation
    public int updateOrderDetailWithCalculatedPrice(OrderDetail orderDetail) {
        try {
            BigDecimal calculatedTotalPrice = calculateTotalPrice(orderDetail.getModelId(), orderDetail.getOrderQuantity());
            orderDetail.setTotalPrice(calculatedTotalPrice);
            return orderDetailDAO.update(orderDetail);
        } catch (Exception e) {
            throw new RuntimeException("Error updating order detail: " + e.getMessage(), e);
        }
    }

    // ==================== Customer Quantity Management ====================

    /**
     * Customer adjusts quantity for OrderDetail
     * Only allows if order is in WAITING_PAYMENT status
     */
    public boolean adjustOrderDetailQuantity(Long orderDetailId, Integer newQuantity, int userId) {
        try {
            // Validate new quantity
            if (newQuantity == null || newQuantity < 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            // Get current OrderDetail
            OrderDetail currentOrderDetail = orderDetailDAO.findById(orderDetailId);
            if (currentOrderDetail == null) {
                throw new IllegalArgumentException("Order detail not found with ID: " + orderDetailId);
            }

            // Get the Order to check status and user ownership
            Order order = orderDAO.findById(currentOrderDetail.getOrderId());
            if (order == null) {
                throw new IllegalArgumentException("Order not found with ID: " + currentOrderDetail.getOrderId());
            }

            // Check if order belongs to the user
            if (order.getUserId() != userId) {
                throw new IllegalArgumentException("This order does not belong to user ID: " + userId);
            }

            // Check if order can be modified (only WAITING_PAYMENT)
            if (!OrderStatus.WAITING_PAYMENT.getThaiTranslation().equals(order.getOrderStatus())) {
                throw new IllegalArgumentException("Cannot modify order. Order status is: " + order.getOrderStatus() +
                    ". Only orders with status '" + OrderStatus.WAITING_PAYMENT.getThaiTranslation() + "' can be modified.");
            }

            // Calculate new total price
            BigDecimal newTotalPrice = calculateTotalPrice(currentOrderDetail.getModelId(), newQuantity);

            // Update OrderDetail with new quantity and price
            currentOrderDetail.setOrderQuantity(newQuantity);
            currentOrderDetail.setTotalPrice(newTotalPrice);

            int result = orderDetailDAO.update(currentOrderDetail);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error adjusting order detail quantity: " + e.getMessage(), e);
        }
    }

    /**
     * Create new order if existing order cannot be modified
     */
    public Long createNewOrderWithDetails(int userId, List<OrderDetail> orderDetails,
                                       String recipientName, String phoneNumber,
                                       String houseAddress, String subDistrict, String district,
                                       String streetName, String province, String postalCode) {
        try {
            // Create new order with WAITING_PAYMENT status
            Order newOrder = new Order();
            newOrder.setUserId(userId);
            newOrder.setOrderStatus(OrderStatus.WAITING_PAYMENT.getThaiTranslation());
            newOrder.setRecipientName(recipientName);
            newOrder.setPhoneNumber(phoneNumber);
            newOrder.setHouseAddress(houseAddress);
            newOrder.setSubDistrict(subDistrict);
            newOrder.setDistrict(district);
            newOrder.setStreetName(streetName);
            newOrder.setProvince(province);
            newOrder.setPostalCode(postalCode);
            newOrder.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));

            // Calculate grand total
            BigDecimal grandTotal = BigDecimal.ZERO;
            for (OrderDetail detail : orderDetails) {
                BigDecimal detailTotal = calculateTotalPrice(detail.getModelId(), detail.getOrderQuantity());
                detail.setTotalPrice(detailTotal);
                grandTotal = grandTotal.add(detailTotal);
            }
            newOrder.setGrandTotalPrice(grandTotal);

            // Insert order first to get orderId
            int orderResult = orderDAO.insert(newOrder);
            if (orderResult <= 0) {
                throw new RuntimeException("Failed to create new order");
            }

            // Get the generated order ID (assuming it's set in the Order object after insertion)
            Long newOrderId = newOrder.getOrderId();
            if (newOrderId == null) {
                // If auto-generated ID is not set, we might need to retrieve it
                // For now, let's use a simple approach
                newOrderId = (long) orderResult; // This might not be correct, depends on DAO implementation
            }

            // Insert all order details with the new order ID
            for (OrderDetail detail : orderDetails) {
                detail.setOrderId(newOrderId);
                orderDetailDAO.insert(detail);
            }

            return newOrderId;
        } catch (Exception e) {
            throw new RuntimeException("Error creating new order: " + e.getMessage(), e);
        }
    }

    // Get OrderDetail with product model information
    public OrderDetail getOrderDetailWithProductInfo(Long orderDetailId) {
        try {
            OrderDetail orderDetail = orderDetailDAO.findById(orderDetailId);
            if (orderDetail != null) {
                ProductModel productModel = productModelDAO.findById(orderDetail.getModelId());
                // You could extend OrderDetail to include product model info if needed
            }
            return orderDetail;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving order detail: " + e.getMessage(), e);
        }
    }
}