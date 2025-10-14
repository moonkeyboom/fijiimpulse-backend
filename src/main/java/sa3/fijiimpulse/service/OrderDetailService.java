package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.OrderDetailDAO;
import sa3.fijiimpulse.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderDetailService {
    private final OrderDetailDAO orderDetailDAO;

    public OrderDetailService(OrderDetailDAO orderDetailDAO) {
        this.orderDetailDAO = orderDetailDAO;
    }

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailDAO.findAll();
    }

    public OrderDetail getOrderDetailById(long id) {
        return orderDetailDAO.findById(id);
    }

    public int createOrderDetail(OrderDetail od) {
        return orderDetailDAO.insert(od);
    }

    public int updateOrderDetail(OrderDetail od) {
        return orderDetailDAO.update(od);
    }

    public int deleteOrderDetail(long id) {
        return orderDetailDAO.delete(id);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(long orderId) {
        return orderDetailDAO.findByOrderId(orderId);
    }

// ---------------***********************------------------------
    public int addOrUpdateOrderDetail(long orderId, int modelId, int quantity, BigDecimal price) {
        OrderDetail existing = orderDetailDAO.findByOrderIdAndModelId(orderId, modelId);
        if (existing != null) {
            existing.setOrderQuantity(existing.getOrderQuantity() + quantity);
            existing.setTotalPrice(existing.getTotalPrice().add(price.multiply(new BigDecimal(quantity))));
            return orderDetailDAO.update(existing);
        } else {
            OrderDetail od = new OrderDetail();
            od.setOrderId(orderId);
            od.setModelId(modelId);
            od.setOrderQuantity(quantity);
            od.setTotalPrice(price.multiply(new BigDecimal(quantity)));
            return orderDetailDAO.insert(od);
        }
    }

}
