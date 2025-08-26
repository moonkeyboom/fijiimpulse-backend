package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.OrderDetail;
import sa3.fijiimpulse.service.OrderDetailService;

import java.util.List;

@RestController
@RequestMapping("/order-details")
public class OrderDetailController {
    private final OrderDetailService service;

    public OrderDetailController(OrderDetailService service) { this.service = service; }

    @PostMapping
    public String create(@RequestBody OrderDetail od) { service.create(od); return "OrderDetail created"; }

    @PutMapping("/{orderId}/{serialNo}")
    public String update(@PathVariable Long orderId, @PathVariable String serialNo, @RequestBody OrderDetail od) {
        od.setOrderId(orderId); od.setSerialNo(serialNo);
        service.update(od); return "OrderDetail updated";
    }

    @DeleteMapping("/{orderId}/{serialNo}")
    public String delete(@PathVariable Long orderId, @PathVariable String serialNo) {
        service.delete(orderId, serialNo); return "OrderDetail deleted";
    }

    @GetMapping("/{orderId}/{serialNo}")
    public OrderDetail get(@PathVariable Long orderId, @PathVariable String serialNo) {
        return service.get(orderId, serialNo);
    }

    @GetMapping
    public List<OrderDetail> getAll() { return service.getAll(); }
}