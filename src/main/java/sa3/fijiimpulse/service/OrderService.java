package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.entity.Order;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.repositories.OrderRepository;
import sa3.fijiimpulse.repositories.PaymentRepository;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository) { this.repository = repository; }

    public int create(Order o) { return repository.save(o); }
//    public int update(Order o) { return repository.update(o); }
//    public int delete(Long orderId) { return repository.delete(orderId); }
    public Order get(Long orderId) { return repository.findById(orderId); }
//    public List<Order> getAll() { return repository.findAll(); }
}
