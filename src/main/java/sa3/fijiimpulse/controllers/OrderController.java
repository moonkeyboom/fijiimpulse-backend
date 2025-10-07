package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sa3.fijiimpulse.entity.User;
import sa3.fijiimpulse.service.OrderService;

@RestController
@RequestMapping
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String createUser(@RequestBody User user) {
//        orderService.createOrder(user);
        return "Order created successfully!";
    }

}
