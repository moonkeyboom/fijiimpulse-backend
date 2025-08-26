package sa3.fijiimpulse.entity;

public class Supplier {
    private Long supplierId;
    private String companyName;
    private String phoneNumber;
    private String supplierEmail;

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSupplierEmail() { return supplierEmail; }
    public void setSupplierEmail(String supplierEmail) { this.supplierEmail = supplierEmail; }

    public Supplier(Long supplierId, String companyName, String phoneNumber, String supplierEmail) {

        this.supplierId = supplierId;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.supplierEmail = supplierEmail;
    }
}

