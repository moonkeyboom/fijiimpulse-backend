package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.entity.OrderDetail;
import sa3.fijiimpulse.repositories.OrderDetailRepository;

import java.util.List;

@Service
public class OrderDetailService {
    private final OrderDetailRepository repository;

    public OrderDetailService(OrderDetailRepository repository) { this.repository = repository; }

    public int create(OrderDetail od) { return repository.save(od); }
    public int update(OrderDetail od) { return repository.update(od); }
    public int delete(Long orderId, String serialNo) { return repository.delete(orderId, serialNo); }
    public OrderDetail get(Long orderId, String serialNo) { return repository.findById(orderId, serialNo); }
    public List<OrderDetail> getAll() { return repository.findAll(); }
}
