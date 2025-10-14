-- ========================================
-- Test Data สำหรับ Use Case 2M
-- ========================================

-- 1. สร้าง User (Manufacturer และ Customer)
INSERT INTO USERS (Email, Username, Role, Password_hash) VALUES
('manufacturer@test.com', 'manufacturer1', 'MANUFACTURER', 'hashed_password_123'),
('customer1@test.com', 'customer1', 'CUSTOMER', 'hashed_password_456'),
('customer2@test.com', 'customer2', 'CUSTOMER', 'hashed_password_789');

-- 2. สร้าง Recipe และ Product Model
INSERT INTO RECIPE (Recipe_id) VALUES (1), (2);

INSERT INTO PRODUCT_MODEL (Model_id, Recipe_id, Model_name, Price, Model_image, Description) VALUES
(1, 1, 'Premium Soap Bar', 150.00, 'soap1.jpg', 'Premium handmade soap'),
(2, 2, 'Lavender Soap', 120.00, 'soap2.jpg', 'Lavender scented soap');

-- 3. สร้าง Order ที่รอตรวจสอบหลักฐานการชำระเงิน
INSERT INTO ORDERS (Order_id, User_id, Order_status, Recipient_name, Phone_number, District, House_address, Sub_district, Street_name, Province, Postal_code, Grand_total_price) VALUES
(1, 2, 'รอตรวจสอบหลักฐานการชำระเงิน', 'สมชาย ใจดี', '0812345678', 'บางเขน', '123/45', 'ท่าแร้ง', 'พหลโยธิน', 'กรุงเทพมหานคร', '10220', 300.00),
(2, 2, 'รอตรวจสอบหลักฐานการชำระเงิน', 'สมหญิง รักดี', '0823456789', 'ดอนเมือง', '456/78', 'สีกัน', 'วิภาวดีรังสิต', 'กรุงเทพมหานคร', '10210', 240.00),
(3, 3, 'ได้รับการยืนยันการชำระเงิน', 'ทดสอบ สำเร็จ', '0834567890', 'ลาดพร้าว', '789/12', 'ลาดพร้าว', 'พหลโยธิน', 'กรุงเทพมหานคร', '10230', 150.00);

-- 4. สร้าง Payment (หลักฐานการชำระเงิน)
INSERT INTO PAYMENT (Order_id, Total_amount, Payment_receipt) VALUES
(1, 300.00, 'receipt_001.jpg'),
(2, 240.00, 'receipt_002.jpg'),
(3, 150.00, 'receipt_003.jpg');

-- 5. สร้าง Product Item
INSERT INTO PRODUCT_ITEM (Serial_no, Model_id, Order_id) VALUES
('SOAP-2025-001', 1, 1),
('SOAP-2025-002', 1, 1),
('SOAP-2025-003', 2, 2),
('SOAP-2025-004', 2, 2),
('SOAP-2025-005', 1, 3);

-- 6. สร้างข้อมูล Material สำหรับ Use Case 3M, 4M
INSERT INTO SUPPLIER_DETAILS (Company_name, Phone_number, Supplier_email) VALUES
('บริษัท วัตถุดิบดี จำกัด', '021234567', 'supplier1@test.com'),
('บริษัท คุณภาพสูง จำกัด', '021234568', 'supplier2@test.com');

INSERT INTO MATERIAL (Supplier_id, Material_name, Stock_quantity, Material_image) VALUES
(1, 'น้ำมันมะพร้าว', 50, 'coconut_oil.jpg'),
(1, 'โซดาไฟ', 30, 'lye.jpg'),
(2, 'น้ำมันหอมระเหย', 20, 'essential_oil.jpg'),
(2, 'สีธรรมชาติ', 15, 'natural_color.jpg');

INSERT INTO RECIPE_MATERIAL (Recipe_id, Material_id, Required_quantity) VALUES
(1, 1, 5),
(1, 2, 2),
(1, 3, 1),
(2, 1, 4),
(2, 3, 2),
(2, 4, 1);
