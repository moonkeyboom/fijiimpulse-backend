package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.repositories.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) { this.repository = repository; }

    public int create(Payment p) { return repository.save(p); }
    public int update(Payment p) { return repository.update(p); }
    public int delete(Long paymentId) { return repository.delete(paymentId); }
    public Payment get(Long paymentId) { return repository.findById(paymentId); }
    public List<Payment> getAll() { return repository.findAll(); }
}