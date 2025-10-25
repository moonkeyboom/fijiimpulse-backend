package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.dao.MaterialDAO;
import sa3.fijiimpulse.dao.ProductItemDAO;
import sa3.fijiimpulse.entity.Material;
import sa3.fijiimpulse.entity.ProductItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class MaterialService {

    private final MaterialDAO materialDAO;
    private final ProductItemDAO productItemDAO;

    private final String uploadDirAbsolute = System.getProperty("user.dir") + "/uploads/material-image";
    private final String uploadDirRelative = "uploads/material-image";

    public MaterialService(MaterialDAO materialDAO, ProductItemDAO productItemDAO) {
        this.materialDAO = materialDAO;
        this.productItemDAO = productItemDAO;
    }

    // ==================== Use Case 3M: ตรวจสอบทรัพยากร ====================

    public List<ProductItem> getAllProductItems() {
        return productItemDAO.findAll();
    }

    public List<Material> getAllMaterials() {
        return materialDAO.findAll();
    }

    public Material getMaterialById(int materialId) {
        return materialDAO.findById(materialId);
    }

    // ==================== Use Case 4M: ได้รับวัสดุอุปกรณ์ ====================

    public boolean receiveMaterial(int materialId, int receivedQuantity) {
        if (receivedQuantity <= 0) {
            return false;
        }

        int rowsAffected = materialDAO.addMaterialStock(materialId, receivedQuantity);
        return rowsAffected > 0;
    }

    public boolean reduceMaterial(int materialId, int reduceQuantity) {
        if (reduceQuantity <= 0) {
            return false;
        }

        int rowsAffected = materialDAO.reduceMaterialStock(materialId, reduceQuantity);
        return rowsAffected > 0;
    }

    // ==================== CRUD Operations ====================

    public int createMaterial(Material material) {
        return materialDAO.insert(material);
    }

    public int updateMaterial(Material material) {
        return materialDAO.update(material);
    }

    public int deleteMaterial(int materialId) {
        return materialDAO.delete(materialId);
    }

    // ==================== Upload Material with Image ====================

    public void saveMaterialWithImage(int supplierId, String materialName, int stockQuantity, MultipartFile file) throws IOException {
        Material material = new Material();
        material.setSupplierId(supplierId);
        material.setMaterialName(materialName);
        material.setStockQuantity(stockQuantity);

        if (file != null && !file.isEmpty()) {
            Path uploadPath = Paths.get(uploadDirAbsolute);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String filename = UUID.randomUUID().toString() + ext;
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            material.setMaterialImagePath(uploadDirRelative + "/" + filename);
        }

        materialDAO.insert(material);
    }

    public void updateMaterialWithImage(int materialId, int supplierId, String materialName, int stockQuantity, MultipartFile file) throws IOException {
        Material material = materialDAO.findById(materialId);
        material.setSupplierId(supplierId);
        material.setMaterialName(materialName);
        material.setStockQuantity(stockQuantity);

        if (file != null && !file.isEmpty()) {
            // ลบไฟล์เก่า
            if (material.getMaterialImagePath() != null) {
                Path oldFile = Paths.get(System.getProperty("user.dir"), material.getMaterialImagePath());
                if (Files.exists(oldFile)) Files.delete(oldFile);
            }

            Path uploadPath = Paths.get(uploadDirAbsolute);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String filename = UUID.randomUUID().toString() + ext;
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            material.setMaterialImagePath(uploadDirRelative + "/" + filename);
        }

        materialDAO.update(material);
    }
}
