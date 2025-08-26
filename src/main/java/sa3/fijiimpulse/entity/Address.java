package sa3.fijiimpulse.entity;


public class Address {
    private String recipientName;
    private String houseAddress;
    private String streetName;
    private String subDistrict;
    private String district;
    private String province;
    private String postalCode;

    public Address() {}

    public Address(String recipientName, String houseAddress, String streetName, String subDistrict,
                   String district, String province, String postalCode) {
        this.recipientName = recipientName;
        this.houseAddress = houseAddress;
        this.streetName = streetName;
        this.subDistrict = subDistrict;
        this.district = district;
        this.province = province;
        this.postalCode = postalCode;
    }

    // getters & setters
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getHouseAddress() { return houseAddress; }
    public void setHouseAddress(String houseAddress) { this.houseAddress = houseAddress; }

    public String getStreetName() { return streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }

    public String getSubDistrict() { return subDistrict; }
    public void setSubDistrict(String subDistrict) { this.subDistrict = subDistrict; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
}