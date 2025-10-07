package sa3.fijiimpulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Material {
    private int materialId;
    private int supplierId;
    private String materialName;
    private int stockQuantity;
    private String materialImage;
    private java.sql.Timestamp lastUpdated;

    public Material(int materialId, int supplierId, String materialName, int stockQuantity, String materialImage, Timestamp lastUpdated) {
        this.materialId = materialId;
        this.supplierId = supplierId;
        this.materialName = materialName;
        this.stockQuantity = stockQuantity;
        this.materialImage = materialImage;
        this.lastUpdated = lastUpdated;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getMaterialImage() {
        return materialImage;
    }

    public void setMaterialImage(String materialImage) {
        this.materialImage = materialImage;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Material{" +
                "materialId=" + materialId +
                ", supplierId=" + supplierId +
                ", materialName='" + materialName + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", materialImage='" + materialImage + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
