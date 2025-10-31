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
import java.nio.file.Files;
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


//    ใช้ตัวนี้
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
    public ResponseEntity<byte[]> downloadPaymentReceipt(@PathVariable long orderId) throws IOException {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        if (payment == null || payment.getPaymentReceipt() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                    .body("Receipt not found".getBytes());
        }

        File imgFile = new File(payment.getPaymentReceipt());
        if (!imgFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                    .body("Receipt file not found".getBytes());
        }

        String filename = imgFile.getName();
        byte[] fileBytes = Files.readAllBytes(imgFile.toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", filename);

        String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        if ("png".equals(ext)) headers.setContentType(MediaType.IMAGE_PNG);
        else if ("jpg".equals(ext) || "jpeg".equals(ext)) headers.setContentType(MediaType.IMAGE_JPEG);
        else headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}/view")
    public ResponseEntity<byte[]> viewReceipt(@PathVariable long orderId) throws IOException {
        Payment payment = paymentService.getPaymentReceiptByOrderId(orderId);
        if (payment == null || payment.getPaymentReceipt() == null) return ResponseEntity.notFound().build();

        File imgFile = new File(payment.getPaymentReceipt());
        if (!imgFile.exists()) return ResponseEntity.notFound().build();

        byte[] fileBytes = Files.readAllBytes(imgFile.toPath());

        HttpHeaders headers = new HttpHeaders();
        String ext = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1).toLowerCase();
        if ("png".equals(ext)) headers.setContentType(MediaType.IMAGE_PNG);
        else if ("jpg".equals(ext) || "jpeg".equals(ext)) headers.setContentType(MediaType.IMAGE_JPEG);
        else headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }




}
