package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.OrderDAO;
import sa3.fijiimpulse.entity.Order;

import java.util.List;

@Service
public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public Order getOrderById(long id) {
        return orderDAO.findById(id);
    }

    public int createOrder(Order order) {
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
}
