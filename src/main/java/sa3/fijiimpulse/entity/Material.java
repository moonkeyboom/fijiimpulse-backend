package sa3.fijiimpulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

import java.sql.Timestamp;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Material {
    private int materialId;
    private int supplierId;
    private String materialName;
    private int stockQuantity;

    @Column(name = "Material_image")
    private byte[] materialImage; // ใช้ byte[] เพื่อเก็บภาพใน DB

    private java.sql.Timestamp lastUpdated;

    public Material() {
    }

    public Material(int materialId, int supplierId, String materialName, int stockQuantity, byte[] materialImage, Timestamp lastUpdated) {
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

    public byte[] getMaterialImage() {
        return materialImage;
    }

    public void setMaterialImage(byte[] materialImage) {
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
                ", materialImage=" + (materialImage != null ? "[BLOB data]" : "null") +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
