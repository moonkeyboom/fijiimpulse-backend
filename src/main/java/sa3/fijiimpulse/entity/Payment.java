package sa3.fijiimpulse.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private double totalAmount;
    private LocalDate paymentDate;
    private String paymentType;
    private String paymentStatus;

    @OneToOne(mappedBy = "payment")
    private Order order;

    public Payment() {}

}
