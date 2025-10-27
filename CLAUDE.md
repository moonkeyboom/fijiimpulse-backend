# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Running the Application
```bash
# Start the application with Spring Boot Maven plugin
mvn spring-boot:run

# Start with development profile for hot reloading (DevTools enabled)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Building and Testing
```bash
# Clean compile
mvn clean compile

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=FijiimpulseApplicationTests

# Package the application
mvn clean package

# Skip tests during packaging
mvn clean package -DskipTests
```

## Architecture Overview

This is a **Spring Boot REST API** for a handmade soap manufacturing e-commerce platform using a **4-tier hybrid MVC architecture** with **JDBC Template** (NOT JPA/Hibernate).

### Core Architectural Pattern

**Request Flow**: HTTP Request → Controller → Service → DAO → Database

**Tiers**:
- **Controller Layer** (`/controllers`): REST endpoints, HTTP request/response handling
- **Service Layer** (`/service`): Business logic, orchestration, transaction management
- **DAO Layer** (`/dao`): Data access using JdbcTemplate with manual SQL
- **Entity Layer** (`/entity`): Plain Java objects (no JPA annotations)

### Data Access Architecture

**CRITICAL**: Uses **JDBC Template** despite having JPA dependency in POM
- Manual SQL writing with JdbcTemplate and custom row mappers
- Entities are simple POJOs without ORM annotations
- Raw SQL queries throughout the application
- Database schema auto-updates via `spring.jpa.hibernate.ddl-auto=update`

### Authentication System

**Session-based authentication** (NOT JWT):
- Custom implementation using HttpSession
- BCrypt password hashing via `spring-security-crypto`
- Basic role system with string-based roles
- Session lifecycle managed in `AuthController`

### Business Domain Architecture

**Production-Oriented E-commerce Design**:

```
Supplier → Material → Recipe → ProductModel → ProductItem → Order → OrderDetail → Payment
```

**Key Domain Relationships**:
- **Production Integration**: Materials linked to suppliers with stock tracking
- **Recipe System**: Products use recipes defining material requirements
- **Serial Number Management**: ProductItems have unique serial numbers for traceability
- **Status-Based Order Flow**: Complex order status transitions with Thai translations

### Order Status Workflow (Thai)

Located in `service/enums/OrderStatus.java`:
- `รอชำระเงินและอัปโหลดหลักฐานการชำระเงิน` → Payment pending
- `รอตรวจสอบหลักฐานการชำระเงิน` → Receipt verification pending
- `ได้รับการยืนยันการชำระเงิน` → Payment approved
- `กำลังดำเนินการผลิต` → In production
- `ผลิตสำเร็จแล้ว` → Production complete
- `เตรียมจัดส่ง` → Ready to ship
- `กำลังจัดส่ง` → Shipped
- `จัดส่งสำเร็จแล้ว` → Delivered

## Configuration

### Application Properties
- Server runs on port 8081
- MySQL connection: `jdbc:mysql://localhost:3306/fiji-impulse-1`, username `root`
- File upload limits: 50MB for payment receipts
- DevTools enabled for hot reloading

### Database Setup
Use provided `test-data.sql` for initial test data including:
- User accounts with different roles
- Product recipes and models
- Sample orders with various statuses
- Payment records and serialized product items
- Materials, suppliers, and recipe relationships

## Key Architectural Decisions

### Production-Ready Features
- **Material Inventory Management**: Built-in stock checking and shortage detection
- **Production Status Tracking**: Detailed order status flow from payment to delivery
- **Resource Validation**: `checkMaterialsForOrder()` validates material sufficiency before production
- **Serial Number Tracking**: Each product item has unique serial for quality control

### Technical Implementation
- **Database-Centric**: All logic is DAO-driven, not ORM-centric
- **File Path Storage**: Images stored as file paths rather than BLOBs
- **Custom Image Processing**: Dedicated `ImageUtils` for automatic image compression
- **Thai Localization**: Built-in Thai language support for order statuses and user messages

## Important Gotchas

1. **JDBC over JPA**: Despite `spring-boot-starter-data-jpa` dependency, code uses pure JDBC Template
2. **No Spring Security**: Custom session-based authentication implementation
3. **Lombok Usage**: Some entities have commented Lombok annotations but use manual getters/setters
4. **Mixed Entity Patterns**: Inconsistent use of annotations across entities
5. **Auto Schema Updates**: Database schema auto-updates on startup via `ddl-auto=update`
6. **Production Workflow**: Architecture supports complex manufacturing processes beyond simple e-commerce