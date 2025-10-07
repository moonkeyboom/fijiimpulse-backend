package sa3.fijiimpulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class SupplierDetails {
    private int supplierId;
    private String companyName;
    private String phoneNumber;
    private String supplierEmail;

    public SupplierDetails(int supplierId, String companyName, String phoneNumber, String supplierEmail) {
        this.supplierId = supplierId;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.supplierEmail = supplierEmail;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    @Override
    public String toString() {
        return "SupplierDetails{" +
                "supplierId=" + supplierId +
                ", companyName='" + companyName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", supplierEmail='" + supplierEmail + '\'' +
                '}';
    }
}
