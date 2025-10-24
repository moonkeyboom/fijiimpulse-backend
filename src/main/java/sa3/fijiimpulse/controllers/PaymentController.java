package sa3.fijiimpulse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.service.PaymentService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // ✅ GET: ดึงทั้งหมด
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // ✅ GET: ดึงตาม Payment ID
    @GetMapping("/{paymentId}")
    public Payment getPaymentById(@PathVariable long paymentId) {
        return paymentService.getPaymentById(paymentId);
    }

    // ✅ GET: ดึงตาม Order ID
    @GetMapping("/order/{orderId}")
    public Payment getPaymentByOrderId(@PathVariable long orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }

    // ✅ POST: สร้าง Payment ธรรมดา
    @PostMapping
    public String createPayment(@RequestBody Payment payment) {
        paymentService.createPayment(payment);
        return "Payment created successfully!";
    }

    // ✅ PUT: แก้ไข Payment
    @PutMapping("/{paymentId}")
    public String updatePayment(@PathVariable long paymentId, @RequestBody Payment payment) {
        payment.setPaymentId(paymentId);
        paymentService.updatePayment(payment);
        return "Payment updated successfully!";
    }

    // ✅ DELETE: ลบ Payment
    @DeleteMapping("/{paymentId}")
    public String deletePayment(@PathVariable long paymentId) {
        paymentService.deletePayment(paymentId);
        return "Payment deleted successfully!";
    }

    // ✅ POST: อัปโหลดหลักฐานการชำระเงิน (MultipartFile)
    @PostMapping("/upload")
    public String uploadPayment(
            @RequestParam long orderId,
            @RequestParam BigDecimal totalAmount,
            @RequestParam String paymentDate,   // รับเป็น String
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        Timestamp ts;
        if (paymentDate.contains("T")) {
            // แปลง "2025-10-23T12:00:00" → "2025-10-23 12:00:00"
            ts = Timestamp.valueOf(paymentDate.replace("T", " "));
        } else {
            ts = Timestamp.valueOf(paymentDate);
        }

        paymentService.savePayment(orderId, totalAmount, ts, file);
        return "Payment uploaded successfully for Order ID " + orderId;
    }

}
