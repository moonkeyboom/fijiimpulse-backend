package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.PaymentDAO;
import sa3.fijiimpulse.entity.Payment;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.utils.ImageUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
public class PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    // ดึง Payment ทั้งหมด
    public List<Payment> getAllPayments() {
        return paymentDAO.findAll();
    }

    // ดึง Payment ตาม id
    public Payment getPaymentById(long paymentId) {
        return paymentDAO.findById(paymentId);
    }

    // ดึง Payment ตาม order id
    public Payment getPaymentByOrderId(long orderId) {
        return paymentDAO.findByOrderId(orderId);
    }


    // เพิ่ม Payment ใหม่
    public int createPayment(Payment payment) {
        return paymentDAO.insert(payment);
    }

    // แก้ไข Payment
    public int updatePayment(Payment payment) {
        return paymentDAO.update(payment);
    }

    // ลบ Payment
    public int deletePayment(long paymentId) {
        return paymentDAO.delete(paymentId);
    }

//    public void savePayment(long orderId, BigDecimal totalAmount, Timestamp paymentDate, MultipartFile file) throws IOException {
//        // 1️⃣ บันทึกไฟล์เหมือนเดิม
////        String uploadDir = System.getProperty("user.dir") + "/uploads/receipts/";
//        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/uploads/receipts";
//        File directory = new File(uploadDir);
//        if (!directory.exists()) directory.mkdirs();
//
//        String fileName = "receipt_order_" + orderId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        Path filePath = Paths.get(uploadDir, fileName);
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//        // 2️⃣ ตรวจสอบว่ามี Payment สำหรับ order นี้แล้วหรือยัง
//        Payment existingPayment = null;
//        try {
//            existingPayment = paymentDAO.findByOrderId(orderId);
//        } catch (Exception ignored) {}
//
//        if (existingPayment != null) {
//            // อัปเดต Payment เดิม
//            existingPayment.setTotalAmount(totalAmount);
//            existingPayment.setPaymentDate(paymentDate);
//            existingPayment.setPaymentReceipt(filePath.toString());
//            paymentDAO.update(existingPayment);
//        } else {
//            // สร้าง Payment ใหม่
//            Payment payment = new Payment();
//            payment.setOrderId(orderId);
//            payment.setTotalAmount(totalAmount);
//            payment.setPaymentDate(paymentDate);
//            payment.setPaymentReceipt(filePath.toString());
//            paymentDAO.insert(payment);
//        }
//    }
//    // ✅ บันทึกรูปลงฐานข้อมูล (BLOB)
//    public void savePayment(long orderId, BigDecimal totalAmount, Timestamp paymentDate, MultipartFile file) throws IOException {
//        byte[] compressedBytes = ImageUtils.compressAndResizeImage(file, 1024 * 1024); // 1MB
//
//        Payment payment = new Payment();
//        payment.setOrderId(orderId);
//        payment.setTotalAmount(totalAmount);
//        payment.setPaymentDate(paymentDate);
//        payment.setPaymentReceipt(compressedBytes);
//
//        paymentDAO.update(payment);
//    }
    public void savePayment(long orderId, BigDecimal totalAmount, Timestamp paymentDate, MultipartFile file) throws IOException {
        byte[] compressedBytes = null;

        if (file != null && !file.isEmpty()) {
            // บีบอัดและปรับขนาดให้ไม่เกิน 1MB
            compressedBytes = ImageUtils.compressAndResizeImage(file, 1024 * 1024); // 1MB
        }

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setTotalAmount(totalAmount);
        payment.setPaymentDate(paymentDate);
        payment.setPaymentReceipt(compressedBytes);

        paymentDAO.update(payment);
    }


}
