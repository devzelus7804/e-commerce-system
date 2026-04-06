# 🛍️ DA-J2EE E-commerce API

## 📝 Giới thiệu
Dự án **DA-J2EE** là một ứng dụng backend **Spring Boot REST API** cung cấp nền tảng phục vụ cho hệ thống thương mại điện tử. Hệ thống được thiết kế với đầy đủ các tính năng quản lý người dùng, bảo mật JWT, phân quyền theo vai trò (Role-based access control), và quản lý sản phẩm, danh mục cho phía Admin và Frontend.

## ✨ Tính năng nổi bật

### 🔐 Xác thực & Phân quyền (Auth & Security)
*   **Đăng ký & Đăng nhập** an toàn với mật khẩu được mã hóa bằng thuật toán `BCrypt`.
*   Bảo mật các endpoint dựa trên **JWT (JSON Web Tokens)** - kiến trúc Stateless hoàn toàn.
*   Phân quyền chi tiết theo vai trò: `ADMIN` (Toàn quyền), `STAFF` (Nhân viên), `USER` (Khách hàng).

### 🧑‍💼 Quản lý Người dùng (User Management)
*   Truy xuất và cập nhật hồ sơ cá nhân.
*   Quản lý **Địa chỉ giao hàng** (Thêm, Sửa địa chỉ với bảo mật kép kiểm tra quyền sở hữu).

### 📦 Quản lý Sản phẩm & Tồn kho (Product & Stock)
*   **CRUD Sản phẩm & Danh mục:** Quản lý toàn diện từ phía trang Admin. Upload hình ảnh sản phẩm.
*   **Quản lý Biến thể (Product Variants):** Hỗ trợ lưu trữ chi tiết sản phẩm theo Màu sắc (Black, Ivory, Denim...) và Kích thước (XS, S, M, L, XL...).
*   **Theo dõi kho hàng (Stock):** Quản lý số lượng tồn kho dựa trực tiếp trên từng biến thể. Tự động chuyển trạng thái "Còn hàng/Hết hàng" hiển thị lên Frontend.

## 🛠️ Công nghệ sử dụng
*   **Ngôn ngữ:** Java 25
*   **Framework chính:** Spring Boot 4.0.4
*   **Bảo mật:** Spring Security, JWT (`io.jsonwebtoken`)
*   **Cơ sở dữ liệu:** MySQL
*   **ORM:** Spring Data JPA (Hibernate)
*   **Công cụ hỗ trợ:** Lombok, Maven

## 📂 Cấu trúc thư mục chính
*   `config/`: Cấu hình hệ thống (VD: `SecurityConfig.java`).
*   `controller/`: Cung cấp các REST API endpoints. Phân chia ra khu vực Public, Admin và User.
*   `service/`: Chứa toàn bộ logic nghiệp vụ của ứng dụng (Auth, User, Product...).
*   `repository/`: Các interface Spring Data JPA tương tác trực tiếp với Database.
*   `entity/`: Định nghĩa các cấu trúc bảng trong Database (`User`, `UserAddress`, `Product`, ...).
*   `dtos/`: Data Transfer Objects phục vụ chuyển đổi, kiểm soát luồng dữ liệu (Input/Output).
*   `security/`: Bộ lọc `JwtAuthenticationFilter` và `CustomUserPrincipal`.
*   `enums/`: Định nghĩa các giá trị chuẩn hoá như `RoleEnum`, `GenderEnum`.
## 🚀 Hướng dẫn cài đặt và chạy dự án

1. **Yêu cầu môi trường:** Cài đặt Java 25 và Maven. Cài đặt CSDL MySQL.
2. **Cấu hình Database:** Cập nhật url kết nối, username, password MySQL trong thư mục `src/main/resources/application.properties`.
3. **Cài đặt thư viện:** Mở terminal ở thư mục dự án và chạy lệnh:
   ```bash
   mvn clean install
   ```
4. **Khởi động Server:**
   Chạy lệnh maven sau hoặc chạy trực tiếp file `Daj2EeApplication.java` trên IDE:
   ```bash
   mvn spring-boot:run
   ```
5. Hệ thống sẽ khởi chạy và sẵn sàng nhận các kết nối API từ Frontend.