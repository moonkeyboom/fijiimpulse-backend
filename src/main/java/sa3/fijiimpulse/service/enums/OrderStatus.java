package sa3.fijiimpulse.service.enums;

public enum OrderStatus {
 PAYMENT_EVIDENCE_PENDING("รอชำระเงินและอัปโหลดหลักฐานการชำระเงิน"),
 PAYMENT_EVIDENCE_PENDING_AGAIN("หลักฐานการชำระเงินถูกปฏิเสธ รอชำระเงินและอัปโหลดหลักฐานการชำระเงินอีกครั้ง"),
 PENDING_RECEIPT_VERIFICATION("รอตรวจสอบหลักฐานการชำระเงิน"),
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
