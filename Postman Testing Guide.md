# Fiji Impulse API - Postman Testing Guide

This comprehensive guide covers all API endpoints for the Fiji Impulse handmade soap manufacturing e-commerce platform.

## 🚀 Quick Start

1. **Import the Collection**
   - Open Postman
   - Click "Import" → "Select File"
   - Choose `Postman Collection - Fiji Impulse API.postman_collection.json`

2. **Set Environment Variables**
   - Create environment variable `baseUrl` = `http://localhost:8081`
   - For production, change to your server URL

3. **Start the Application**
   ```bash
   mvn spring-boot:run
   ```

## 📋 Complete API Endpoint Coverage

### 🔐 Authentication (`/auth`)
- **POST** `/auth/register` - User registration
- **POST** `/auth/login` - User login (session-based)
- **GET** `/auth/me` - Get current user info
- **POST** `/auth/logout` - User logout

### 🏷️ Product Models (`/product-model`)
- **GET** `/product-model` - Get all product models
- **GET** `/product-model/{id}` - Get product model by ID
- **POST** `/product-model/upload` - Create product model with image
- **PUT** `/product-model/upload` - Update product model with optional image
- **GET** `/product-model/image/{modelId}` - Get product model image

### 🧪 Materials Management (`/materials`)
- **GET** `/materials` - Get all materials
- **GET** `/materials/{materialId}` - Get material by ID
- **POST** `/materials` - Create new material
- **PUT** `/materials/{materialId}` - Update material
- **DELETE** `/materials/{materialId}` - Delete material
- **POST** `/materials/upload` - Upload material with image
- **PUT** `/materials/upload/{materialId}` - Update material with optional image
- **PUT** `/materials/{materialId}/receive` - Increase material stock
- **PUT** `/materials/{materialId}/remove` - Decrease material stock
- **PUT** `/materials/reduce-from-produce` - Reduce materials from production
- **GET** `/materials/image/{materialId}` - Get material image

### 📝 Recipe Materials (`/recipe-material`)
- **GET** `/recipe-material/{model_id}` - Get recipe composition for model

### 📦 Product Items (`/product-items`)
- **GET** `/product-items` - Get all product items
- **GET** `/product-items/{serialNo}` - Get product item by serial number
- **POST** `/product-items` - Create new product item
- **PUT** `/product-items/{serialNo}/order/{orderId}` - Assign item to order
- **POST** `/product-items/adjust-order-items` - Adjust items for order
- **POST** `/product-items/adjust-user-items` - Adjust items for user
- **POST** `/product-items/create-multiple` - Create multiple items
- **GET** `/product-items/user/{userId}` - Get user's product items
- **DELETE** `/product-items/{serialNo}` - Delete product item

### 🛒 Orders Management (`/orders`)
- **GET** `/orders` - Get all orders
- **GET** `/orders/{orderId}` - Get order by ID
- **POST** `/orders/create` - Create new order
- **GET** `/orders/user/{userId}` - Get user's orders
- **GET** `/orders/order/{orderId}/items` - Get order's product items
- **PUT** `/orders/{orderId}/address` - Update complete order address
- **PUT** `/orders/{orderId}/address/partial` - Update partial order address

### 🏭 Order Workflow & Production (`/orders`)
- **GET** `/orders/pending-payment` - Get pending payment orders
- **GET** `/orders/{orderId}/payment` - Get order payment info
- **POST** `/orders/{orderId}/approve` - Approve payment
- **POST** `/orders/{orderId}/reject` - Reject payment
- **POST** `/orders/{orderId}/check-materials` - Check material availability
- **POST** `/orders/{orderId}/mark-produced` - Mark as produced
- **POST** `/orders/{orderId}/prepare-shipping` - Prepare for shipping
- **POST** `/orders/{orderId}/ship` - Ship order
- **POST** `/orders/{orderId}/confirm-delivery` - Confirm delivery

### 📋 Order Details (`/order-details`)
- **POST** `/order-details/add-to-waiting-order` - **[Frontend Single Endpoint]** Add quantity to waiting order (finds/creates order with WAITING_PAYMENT status)
- **GET** `/order-details` - Get all order details
- **GET** `/order-details/{id}` - Get order detail by ID
- **GET** `/order-details/order/{orderId}` - Get details for order
- **POST** `/order-details` - Create order detail
- **POST** `/order-details/calculate-price` - Calculate total price
- **POST** `/order-details/create-with-calculated-price` - Create with auto-calc
- **PUT** `/order-details/{id}` - Update order detail
- **PUT** `/order-details/{id}/adjust-quantity` - Adjust quantity
- **POST** `/order-details/create-new-order` - Create order with details
- **DELETE** `/order-details/{id}` - Delete order detail
- **DELETE** `/order-details/delete/order/{orderId}` - Delete all details for order

### 💳 Payments (`/payments`)
- **GET** `/payments` - Get all payments
- **GET** `/payments/{paymentId}` - Get payment by ID
- **GET** `/payments/order/{orderId}` - Get payment by order ID
- **POST** `/payments` - Create payment record
- **POST** `/payments/upload` - Upload payment with receipt
- **PUT** `/payments/{paymentId}` - Update payment
- **GET** `/payments/receipt/{orderId}` - Download receipt
- **GET** `/payments/view/{orderId}` - View receipt inline
- **DELETE** `/payments/{paymentId}` - Delete payment

## 🧪 Testing Workflows

### 1. User Registration & Login Flow
```bash
1. POST /auth/register - Create new user
2. POST /auth/login - Login user (creates session)
3. GET /auth/me - Verify current user
4. POST /auth/logout - Logout when done
```

### 2. Product Catalog Management
```bash
1. GET /materials - Check available materials
2. GET /recipe-material/{modelId} - Check product recipe
3. POST /product-model/upload - Create new product
4. GET /product-model - Verify product in catalog
5. GET /product-model/image/{modelId} - Check product image
```

### 3. Complete Order Process
```bash
1. POST /orders/create - Create new order
2. POST /order-details - Add items to order
3. POST /payments/upload - Upload payment receipt
4. GET /orders/pending-payment - Check pending orders
5. POST /orders/{orderId}/approve - Approve payment
6. POST /orders/{orderId}/check-materials - Check materials
7. POST /product-items/adjust-order-items - Assign products
8. POST /orders/{orderId}/mark-produced - Mark produced
9. POST /orders/{orderId}/prepare-shipping - Prepare shipping
10. POST /orders/{orderId}/ship - Ship order
11. POST /orders/{orderId}/confirm-delivery - Confirm delivery
```

### 4. Frontend Shopping Cart Workflow
```bash
# Single endpoint to handle all cart operations
POST /order-details/add-to-waiting-order?userId=1&modelId=1&quantity=2

# Business Logic:
# 1. Finds user's order with WAITING_PAYMENT status
# 2. If exists: Adds quantity to existing order detail or creates new detail
# 3. If not exists: Creates new order with WAITING_PAYMENT status + order detail
# 4. Automatically calculates prices and manages order details
```

### 5. Material & Inventory Management
```bash
1. POST /materials/upload - Add new material
2. PUT /materials/{materialId}/receive - Receive stock
3. POST /orders/{orderId}/check-materials - Check availability
4. PUT /materials/reduce-from-produce - Reduce from production
5. GET /materials - Check updated inventory
```

## 📊 Sample Test Data

### User Registration
```json
{
    "username": "testuser123",
    "email": "testuser@example.com",
    "password": "password123"
}
```

### Product Model Creation
```json
// Form data:
recipeId: 1
modelName: "Lavender Dream Soap"
price: 15.99
description: "Handcrafted lavender soap with essential oils"
file: [image file]
```

### Order Creation
```json
{
    "userId": 1,
    "orderDate": "2025-01-20T10:30:00",
    "orderStatus": "รอชำระเงินและอัปโหลดหลักฐานการชำระเงิน",
    "totalAmount": 159.90,
    "recipientName": "Jane Smith",
    "phoneNumber": "0898765432",
    "houseAddress": "456 Oak Avenue",
    "subDistrict": "Sukhumvit",
    "district": "Khlong Toei",
    "streetName": "Sukhumvit Soi 24",
    "province": "Bangkok",
    "postalCode": "10110"
}
```

### Payment Upload
```json
// Form data:
orderId: 1001
totalAmount: 159.90
paymentDate: "2025-01-20T14:30:00"
file: [receipt image file]
```

### Frontend Add to Cart
```json
// Query Parameters:
userId: 1
modelId: 1
quantity: 2
```

## 🔍 Key Testing Points

### Authentication Testing
- Test session persistence across requests
- Verify role-based access if implemented
- Test login with invalid credentials
- Test session invalidation on logout

### File Upload Testing
- Test image upload for products, materials, and receipts
- Verify file size limits (50MB for receipts)
- Test supported formats (JPG, PNG)
- Test missing file scenarios

### Business Logic Testing
- Test material availability checking before production
- Verify order status transitions work correctly
- Test address validation and updates
- Verify price calculations are accurate

### Error Handling Testing
- Test 404 responses for non-existent resources
- Test validation errors for missing required fields
- Test duplicate resource creation (emails, etc.)
- Test invalid parameter formats

## 🚨 Important Notes

### Session Management
- Uses HttpSession (NOT JWT)
- Sessions persist across Postman requests if same session
- Use `/auth/logout` to clear session when testing

### File Upload Limits
- Receipt images: 50MB max
- Product images: No specified limit
- Material images: No specified limit

### Order Status Workflow (Thai)
1. `รอชำระเงินและอัปโหลดหลักฐานการชำระเงิน` → Payment pending
2. `รอตรวจสอบหลักฐานการชำระเงิน` → Receipt verification pending
3. `ได้รับการยืนยันการชำระเงิน` → Payment approved
4. `กำลังดำเนินการผลิต` → In production
5. `ผลิตสำเร็จแล้ว` → Production complete
6. `เตรียมจัดส่ง` → Ready to ship
7. `กำลังจัดส่ง` → Shipped
8. `จัดส่งสำเร็จแล้ว` → Delivered

### Database Connection
- MySQL on localhost:3306
- Database: `fiji-impulse-1`
- Username: `root`
- Auto schema updates enabled

## 🔧 Troubleshooting

### Common Issues
1. **Connection Refused**: Ensure Spring Boot application is running on port 8081
2. **Session Issues**: Use same browser tab/incognito window for session persistence
3. **File Upload Fails**: Check file path and ensure file exists
4. **404 Errors**: Verify endpoint URLs match exactly
5. **Database Errors**: Ensure MySQL is running and database exists

### Debug Tips
- Check Spring Boot console logs for errors
- Use Postman Console for request/response details
- Verify database tables are created correctly
- Check file upload permissions

## 📈 Performance Testing

### Load Testing Scenarios
1. **Concurrent User Registrations**: Test multiple users registering simultaneously
2. **Order Processing**: Test multiple orders being processed
3. **File Uploads**: Test concurrent image uploads
4. **Inventory Updates**: Test material stock updates during production

### Recommended Load Levels
- **Light**: 10 concurrent users, 100 requests/minute
- **Medium**: 50 concurrent users, 500 requests/minute
- **Heavy**: 100+ concurrent users, 1000+ requests/minute

This comprehensive Postman collection covers all 50+ API endpoints across 8 different functional areas of the Fiji Impulse e-commerce platform.