package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.dao.MaterialDAO;
import sa3.fijiimpulse.dao.ProductItemDAO;
import sa3.fijiimpulse.entity.Material;
import sa3.fijiimpulse.entity.ProductItem;

import java.io.IOException;
import java.util.List;

@Service
public class MaterialService {

    private final MaterialDAO materialDAO;
    private final ProductItemDAO productItemDAO;

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
        byte[] fileBytes = file.getBytes(); // แปลงไฟล์เป็น byte[]

        Material material = new Material();
        material.setSupplierId(supplierId);
        material.setMaterialName(materialName);
        material.setStockQuantity(stockQuantity);
        material.setMaterialImage(fileBytes); // เก็บรูปเป็น byte[]

        materialDAO.insert(material);
    }

    public void updateMaterialWithImage(int materialId, int supplierId, String materialName, int stockQuantity, MultipartFile file) throws IOException {
        byte[] fileBytes = file != null ? file.getBytes() : null;

        Material material = new Material();
        material.setMaterialId(materialId);
        material.setSupplierId(supplierId);
        material.setMaterialName(materialName);
        material.setStockQuantity(stockQuantity);

        if (fileBytes != null) {
            material.setMaterialImage(fileBytes);
        } else {
            // ถ้าไม่มีไฟล์ใหม่ ให้ใช้รูปเดิม
            Material existing = materialDAO.findById(materialId);
            material.setMaterialImage(existing.getMaterialImage());
        }

        materialDAO.update(material);
    }

}
