package sa3.fijiimpulse.service;

import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.MaterialDAO;
import sa3.fijiimpulse.dao.ProductItemDAO;
import sa3.fijiimpulse.entity.Material;
import sa3.fijiimpulse.entity.ProductItem;

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
}
