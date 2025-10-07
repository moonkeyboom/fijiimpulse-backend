package sa3.fijiimpulse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDetails {
    private int supplierId;
    private String companyName;
    private String phoneNumber;
    private String supplierEmail;
}
