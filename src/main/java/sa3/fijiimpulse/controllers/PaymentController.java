package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public String create(@RequestBody Payment p) {
        service.create(p);
        return "Payment created";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody Payment p) {
        p.setPaymentId(id); service.update(p);
        return "Payment updated";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Payment deleted";
    }

    @GetMapping("/{id}")
    public Payment get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<Payment> getAll() {
        return service.getAll();
    }
}