package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.PaymentDAO;
import sa3.fijiimpulse.dao.OrderDAO;
import sa3.fijiimpulse.entity.Payment;
import sa3.fijiimpulse.service.enums.OrderStatus;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import java.util.List;

@Service
public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final OrderDAO orderDAO;

    private final String uploadDirAbsolute = System.getProperty("user.dir") + "/uploads/payment-image";
    private final String uploadDirRelative = "uploads/payment-image";

    public PaymentService(PaymentDAO paymentDAO, OrderDAO orderDAO) {
        this.paymentDAO = paymentDAO;
        this.orderDAO = orderDAO;
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


    public void savePayment(long orderId, BigDecimal totalAmount, Timestamp paymentDate, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IOException("Payment receipt file is required.");

        Path uploadPath = Paths.get(uploadDirAbsolute);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String ext = "";
        int dotIndex = file.getOriginalFilename().lastIndexOf('.');
        if (dotIndex >= 0) ext = file.getOriginalFilename().substring(dotIndex);
        String filename = "receipt_order_" + orderId + "_" + System.currentTimeMillis() + ext;

        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath.toFile());

        Payment payment = paymentDAO.findByOrderId(orderId);
        if (payment == null) payment = new Payment();

        payment.setOrderId(orderId);
        payment.setTotalAmount(totalAmount);
        payment.setPaymentDate(paymentDate);
        payment.setPaymentReceipt(uploadDirRelative + "/" + filename);

        if (payment.getPaymentId() > 0) paymentDAO.update(payment);
        else paymentDAO.insert(payment);

        // อัปเดตสถานะ Order เป็น "รอตรวจสอบหลักฐานการชำระเงิน"
        orderDAO.updateOrderStatus(orderId, OrderStatus.PENDING_RECEIPT_VERIFICATION.getThaiTranslation());
    }
}
