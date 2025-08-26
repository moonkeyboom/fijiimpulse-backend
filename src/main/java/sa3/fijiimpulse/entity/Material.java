package sa3.fijiimpulse.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Material {
    private Long materialId;
    private String materialName;
    private BigDecimal quantity;
    private String materialStatus;
    private LocalDateTime lastUpdated;
    private Long supplierId;

    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getMaterialStatus() { return materialStatus; }
    public void setMaterialStatus(String materialStatus) { this.materialStatus = materialStatus; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public Material(Long materialId, String materialName, BigDecimal quantity, String materialStatus, LocalDateTime lastUpdated, Long supplierId) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.quantity = quantity;
        this.materialStatus = materialStatus;
        this.lastUpdated = lastUpdated;
        this.supplierId = supplierId;
    }
}

