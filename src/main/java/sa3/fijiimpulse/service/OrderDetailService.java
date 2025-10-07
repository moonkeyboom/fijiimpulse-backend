package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.OrderDetailDAO;
import sa3.fijiimpulse.entity.OrderDetail;

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


}
