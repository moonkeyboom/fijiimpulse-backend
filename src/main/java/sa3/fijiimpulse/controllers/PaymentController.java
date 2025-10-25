package sa3.fijiimpulse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.service.PaymentService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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

    @PostMapping("/upload")
    public String uploadPayment(
            @RequestParam long orderId,
            @RequestParam BigDecimal totalAmount,
            @RequestParam String paymentDate,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        Timestamp ts;

        try {
            // ✅ รองรับทั้งแบบมี timezone และไม่มี
            if (paymentDate.contains("+") || paymentDate.endsWith("Z")) {
                OffsetDateTime odt = OffsetDateTime.parse(paymentDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                ts = Timestamp.from(odt.toInstant());
            } else {
                ts = Timestamp.valueOf(paymentDate.replace("T", " "));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid paymentDate format. Use ISO 8601 or 'yyyy-MM-dd HH:mm:ss'");
        }

        paymentService.savePayment(orderId, totalAmount, ts, file);
        return "Payment uploaded successfully for Order ID " + orderId;
    }


    @GetMapping("/receipt/{orderId}")
    public ResponseEntity<byte[]> downloadPaymentReceipt(@PathVariable long orderId) {
        try {
            Payment payment = paymentService.getPaymentByOrderId(orderId);
            if (payment == null || payment.getPaymentReceipt() == null) {
                // ถ้าไม่มีข้อมูล ให้ส่ง 404
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                        .body("Receipt not found".getBytes());
            }

            String filename = "receipt_order_" + orderId + ".png";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(payment.getPaymentReceipt()); // ✅ ไม่ต้อง .getBytes()

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "Error retrieving receipt: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                    .body(errorMsg.getBytes());
        }
    }

    @GetMapping("/view/{orderId}")
    public ResponseEntity<byte[]> viewReceipt(@PathVariable long orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);

        if (payment == null || payment.getPaymentReceipt() == null) {
            return ResponseEntity.notFound().build();
        }

        // ตั้ง header เพื่อให้ browser แสดงภาพ
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // หรือ MediaType.IMAGE_JPEG ตามไฟล์จริง

        return new ResponseEntity<>(payment.getPaymentReceipt(), headers, HttpStatus.OK);
    }



}
