package sa3.fijiimpulse.utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {

    /**
     * บีบอัดและปรับขนาดรูปอัตโนมัติ
     * รองรับ JPEG/PNG, แก้ปัญหา colorspace/alpha channel
     * รับประกันว่าไฟล์จะไม่เกิน maxBytes
     * @param file MultipartFile
     * @param maxBytes ขนาดสูงสุด (bytes)
     * @return byte[] ของรูปที่บีบอัด
     * @throws IOException
     */
    public static byte[] compressAndResizeImage(MultipartFile file, int maxBytes) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) throw new IOException("Invalid image file");

        // แปลงเป็น RGB สำหรับ alpha channel
        BufferedImage rgbImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = rgbImage.createGraphics();
        g.setColor(Color.WHITE); // สำหรับ background ของ alpha
        g.fillRect(0, 0, rgbImage.getWidth(), rgbImage.getHeight());
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();

        String format = "jpg";
        String filename = file.getOriginalFilename();
        if (filename != null && filename.toLowerCase().endsWith(".png")) {
            format = "png";
        }

        float quality = 0.9f; // เริ่มต้นคุณภาพ
        int width = rgbImage.getWidth();
        int height = rgbImage.getHeight();
        byte[] result;

        // loop ลดขนาดหรือ quality จน < maxBytes
        do {
            BufferedImage resized = resizeImage(rgbImage, width, height);
            result = writeImageToBytes(resized, format, quality);

            if (result.length > maxBytes) {
                if ("jpg".equals(format)) {
                    quality -= 0.05f; // ลด quality 5%
                    if (quality < 0.1f) {
                        width = (int)(width * 0.9);
                        height = (int)(height * 0.9);
                        quality = 0.9f;
                    }
                } else { // PNG ลดขนาดรูปเท่านั้น
                    width = (int)(width * 0.9);
                    height = (int)(height * 0.9);
                }
            } else {
                break;
            }
        } while (true);

        return result;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image scaled = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    private static byte[] writeImageToBytes(BufferedImage image, String format, float quality) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if ("jpg".equals(format)) {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) throw new IllegalStateException("No JPEG writers found");

            ImageWriter writer = writers.next();
            try (MemoryCacheImageOutputStream ios = new MemoryCacheImageOutputStream(baos)) {
                writer.setOutput(ios);
                ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(quality);
                }
                writer.write(null, new IIOImage(image, null, null), param);
            } finally {
                writer.dispose();
            }
        } else {
            ImageIO.write(image, format, baos);
        }

        return baos.toByteArray();
    }
}
