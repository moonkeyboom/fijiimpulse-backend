package sa3.fijiimpulse.service.enums;

public enum OrderStatus {
 WAITING_PAYMENT("รอชำระเงิน"),
 WAITING_VERIFICATION("รอตรวจสอบหลักฐาน"),
 PAYMENT_REJECTED("หลักฐานการชำระเงินถูกปฏิเสธ"),
 PAYMENT_CONFIRMED("ยืนยันการชำระแล้ว"),
 PRODUCTION_COMPLETE("สินค้าผลิตแล้ว"),
 WAITING_SHIPMENT("รอจัดส่ง"),
 SHIPPEDSHIPPING_IN_PROGRESS("กำลังจัดส่ง"),
 SHIPPED("จัดส่งแล้ว");

 private final String thaiTranslation;

 OrderStatus(String thaiTranslation) {
  this.thaiTranslation = thaiTranslation;
 }

 public String getThaiTranslation() {
  return thaiTranslation;
 }
}
