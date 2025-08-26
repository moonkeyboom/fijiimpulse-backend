package sa3.fijiimpulse.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String recipientName;
    private String phoneNumber;
    private String district;
    private String houseAdress;
    private String subDistrict;
    private String streetName;
    private String province;
    private String postalCode;

    public Address() {}

    public Address(String recipientName, String phoneNumber, String district, String houseAdress, String subDistrict, String streetName, String province, String postalCode) {
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.district = district;
        this.houseAdress = houseAdress;
        this.subDistrict = subDistrict;
        this.streetName = streetName;
        this.province = province;
        this.postalCode = postalCode;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getHouseAdress() {
        return houseAdress;
    }

    public void setHouseAdress(String houseAdress) {
        this.houseAdress = houseAdress;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "recipientName='" + recipientName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", district='" + district + '\'' +
                ", houseAdress='" + houseAdress + '\'' +
                ", subDistrict='" + subDistrict + '\'' +
                ", streetName='" + streetName + '\'' +
                ", province='" + province + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
