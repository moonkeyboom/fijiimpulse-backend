package sa3.fijiimpulse.service.enums;

public enum OrderStatus {
 NOT_CREATED("ไม่ถูกสร้าง"),
 PAYMENT_EVIDENCE_PENDING("รอชำระเงินและอัปโหลดหลักฐานการชำระเงิน"),
 PAYMENT_EVIDENCE_PENDING_AGAIN("รอชำระเงินและอัปโหลดหลักฐานการชำระเงินอีกครั้ง"),
 PAYMENT_APPROVED("ได้รับการยืนยันการชำระเงิน"),
 PRODUCED("สินค้าถูกผลิต"),
 READY_TO_SHIP("รอจัดส่งสินค้า"),
 SHIPPED("กำลังจัดส่ง"),
 DELIVERED("จัดส่งสำเร็จ");

 private final String thaiTranslation;

 OrderStatus(String thaiTranslation) {
  this.thaiTranslation = thaiTranslation;
 }

 public String getThaiTranslation() {
  return thaiTranslation;
 }
}
