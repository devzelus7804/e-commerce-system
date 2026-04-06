# Giải thích các thay đổi Frontend/Admin vừa hoàn thành

Tài liệu này tóm tắt nhanh các phần đã chỉnh trong giao diện và trang quản trị, để dễ review và bảo trì.

## 1) Đồng bộ giao diện và font chữ

- Chuẩn hóa typography trong `styles.css` để các khu vực hiển thị nhất quán hơn.
- Sửa lỗi font tiếng Việt bị vỡ dấu ở banner:
  - Tiêu đề hero chuyển sang `Be Vietnam Pro` để hiển thị đúng các dấu như `ờ`, `ệ`, `ạ`.
- Giữ `Sora` cho logo/brand để vẫn có điểm nhấn.

## 2) Nâng cấp trang Account

- Thêm layout tài khoản dạng 2 cột:
  - Sidebar bên trái có các mục: `Hồ sơ`, `Đơn đã mua`, `Phiếu giảm giá`, `Đăng xuất`.
  - Nội dung bên phải đổi theo tab đang chọn.
- Avatar chữ cái được đổi sang icon cố định để phù hợp khi chưa có chức năng upload avatar.

## 3) Admin CRUD cho Sản phẩm và Danh mục

- `AdminPage` đã có form tạo/sửa/xóa cho:
  - `Sản phẩm`
  - `Danh mục`
- Thêm xác nhận khi xóa và refresh danh sách sau thao tác.
- Thêm toast phản hồi thành công/lỗi sau tạo/sửa/xóa.
- Cải thiện helper API phía frontend để bắt lỗi HTTP rõ ràng, tránh báo thành công giả.

## 4) Upload ảnh sản phẩm từ máy

- Trong form Sản phẩm (Admin):
  - Có trường URL ảnh (`imageUrl`).
  - Có nút chọn file từ máy (`input file`).
- Khi chọn file:
  - Ảnh được đọc bằng `FileReader` và chuyển sang data URL.
  - Dữ liệu ảnh được lưu vào `imageUrl` để gửi backend và lưu DB.
- Có khung xem trước ảnh ngay trong form trước khi lưu.

## 5) Sửa luồng dữ liệu Home/Catalog hiển thị sản phẩm

- Frontend chuyển sang gọi API public:
  - `/api/products`
  - `/api/product-variants`
- Bổ sung controller backend `ProductVariantController` cho route public `/api/product-variants`.

## 6) Trạng thái tồn kho cho người dùng

- Sửa logic tồn kho sản phẩm theo tổng `stock` của toàn bộ biến thể, không chỉ biến thể đầu tiên.
- Card sản phẩm hiển thị rõ:
  - `Còn hàng` (badge xanh)
  - `Hết hàng` (badge đỏ)
- Nút thêm giỏ tự disable khi hết hàng.
- Trang catalog có thêm lọc trạng thái:
  - `Tất cả`
  - `Còn hàng`
  - `Hết hàng`

## 7) Nút số lượng khi thêm giỏ (phía người dùng)

- Trên mỗi card sản phẩm có bộ tăng/giảm số lượng `- / +`.
- Khi bấm thêm giỏ sẽ thêm đúng số lượng đã chọn.
- Có giới hạn min/max theo tồn kho hiện có.

## 8) Tối ưu trang Admin theo luồng làm việc

- Điều chỉnh menu module admin theo kiểu dọc bên trái, dễ thao tác hơn.
- Bổ sung hiển thị trạng thái kho trực tiếp trong danh sách sản phẩm admin.
- Xóa module `Thống kê bán được` theo yêu cầu gần nhất.

## 9) Quản lý số lượng theo Biến thể (cách chuẩn)

Theo thiết kế dữ liệu hiện tại, `stock` nằm ở `ProductVariant` (không nằm ở `Product`).

- Đã bật lại module `Biến thể` trong Admin với form đầy đủ:
  - Sản phẩm, màu, size, tên biến thể, SKU, giá, số lượng tồn kho.
- Số lượng được lưu đúng vào `ProductVariant.stock`.

## 10) Bổ sung backend cho Size để form Biến thể chạy đủ

Dự án trước đó chưa có API Size đầy đủ, đã bổ sung:

- `SizeRepository`
- `SizeService`
- `SizeController` (`/api/sizes`)
- `AdminSizeController` (`/api/admin/sizes`)

Nhờ đó dropdown Size trong form Biến thể hoạt động đúng.

## 11) Ràng buộc màu/size theo bộ hiển thị mong muốn

Form tạo biến thể hiện lọc theo đúng bộ đang dùng ở giao diện:

- Màu: `Black`, `Ivory`, `Stone`, `Denim`, `Olive`
- Size: `XS`, `S`, `M`, `L`, `XL`

Nếu DB chưa có đủ các giá trị trên, form sẽ hiện thông báo để bổ sung dữ liệu.
