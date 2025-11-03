# 00P_N04_25_26_KhuongDuy_HongThai
# 🏦 Fund Manager – Ứng Dụng Quản Lý Ngân Sách Nhóm

## 📘 Giới thiệu

**Fund Manager** là ứng dụng web giúp các nhóm (bạn bè, lớp học, câu lạc bộ, tổ chức nhỏ, v.v.) quản lý chi tiêu chung một cách **minh bạch, chính xác và hiệu quả**.  
Hệ thống giúp ghi nhận, phân loại và thống kê các giao dịch thu – chi, hạn chế sai sót và giảm mâu thuẫn trong quản lý tài chính nhóm.

---

## 🎯 Mục tiêu dự án

- Tự động hóa việc ghi chép và phân chia chi tiêu nhóm.  
- Tạo môi trường minh bạch và tiện lợi trong quản lý ngân sách.  
- Hỗ trợ thống kê và báo cáo tài chính theo thời gian.  
- Áp dụng **mô hình MVC** và **nguyên lý OOP** (Encapsulation, Inheritance, Polymorphism, Abstraction, SOLID).  
- Nâng cao kỹ năng lập trình hướng đối tượng và teamwork thực tế.

---

## 👨‍💻 Thành viên nhóm

| Họ và tên | Vai trò | Tỉ lệ đóng góp |
|------------|----------|----------------|
| **Phạm Khương Duy** | Trưởng nhóm – Backend, Database, Kiểm thử | 65% |
| **Dương Hồng Thái** | Frontend, UI/UX, Báo cáo, Demo | 35% |

## ⚙️ Công nghệ sử dụng

| Thành phần | Công nghệ |
|-------------|------------|
| **Backend** | Spring Boot (Java) |
| **Frontend** | Thymeleaf, HTML, CSS, Bootstrap 5 |
| **CSDL** | MySQL (Cloud MySQL – Aiven) |
| **ORM** | Spring Data JPA / Hibernate |
| **Security** | Spring Security (phân quyền Admin – User) |
| **Testing** | JUnit, MockMVC |

---

## 🧩 Kiến trúc hệ thống

Dự án được xây dựng theo mô hình **MVC (Model – View – Controller)**:

- **Model:** Các entity chính gồm `User`, `Group`, `Transaction`, `Member`, `Category`, `Invitation`.  
- **View:** Giao diện động sử dụng **Thymeleaf** và **Bootstrap**.  
- **Controller:** Xử lý yêu cầu người dùng, gọi dịch vụ (Service), tương tác với dữ liệu qua Repository.  

Hệ thống có phân tầng rõ ràng:
- **Controller Layer:** Tiếp nhận và xử lý yêu cầu từ người dùng.  
- **Service Layer:** Chứa logic nghiệp vụ chính (tạo nhóm, thêm giao dịch, duyệt chi, tính toán số dư).  
- **Repository Layer:** Thao tác dữ liệu với MySQL qua JPA.  
- **View Layer:** Hiển thị kết quả, biểu đồ, báo cáo qua Thymeleaf.

---

## 🔑 Các chức năng chính

### 👤 Quản lý người dùng
- Đăng ký, đăng nhập, đổi mật khẩu.  
- Phân quyền **Admin** và **User**.  
- Cập nhật thông tin cá nhân, ảnh đại diện.

### 👥 Quản lý nhóm
- Tạo nhóm mới, chỉnh sửa, xóa nhóm.  
- Gửi lời mời tham gia nhóm qua mã hoặc email.  
- Quản lý danh sách thành viên, vai trò, số lượng.  
- Theo dõi tổng quan quỹ nhóm và báo cáo.

### 💰 Quản lý giao dịch
- Thêm, sửa, xóa giao dịch thu – chi.  
- Gắn danh mục (`Category`) cho giao dịch.  
- Duyệt hoặc từ chối giao dịch (theo quyền Admin).  
- Tự động cập nhật **tổng thu, tổng chi, số dư**.  
- Gửi thông báo đến các thành viên khi có thay đổi.

### 📊 Báo cáo & thống kê
- Biểu đồ trực quan (Line / Bar / Pie).  
- Báo cáo thu chi theo nhóm, thời gian, thành viên.  
- Lọc dữ liệu và xuất báo cáo tổng hợp.  

---

## 🧠 Cơ sở dữ liệu

### Cấu trúc chính

| Bảng | Chức năng | Mối quan hệ |
|------|------------|-------------|
| `users` | Quản lý thông tin người dùng | 1 người có thể thuộc nhiều nhóm |
| `groups` | Quản lý thông tin nhóm | 1 nhóm có nhiều thành viên và giao dịch |
| `group_members` | Liên kết User ↔ Group | Quan hệ N–N |
| `transactions` | Lưu giao dịch thu – chi | 1 nhóm có nhiều giao dịch |
| `group_invites` | Quản lý lời mời tham gia nhóm | 1 nhóm có nhiều lời mời |

Dữ liệu được truy xuất qua **Spring Data JPA**, tự động ánh xạ giữa entity và bảng MySQL.

---

## 🧭 Quy trình hoạt động

1. **Người dùng thao tác** (đăng nhập, tạo nhóm, thêm giao dịch, xem báo cáo).  
2. **Controller** tiếp nhận request và xác thực quyền.  
3. **Service Layer** xử lý logic nghiệp vụ.  
4. **Repository** truy xuất dữ liệu trong MySQL.  
5. **View (Thymeleaf)** hiển thị kết quả và phản hồi người dùng.  

Vòng lặp hoạt động khép kín giữa:  
👉 *User → Controller → Service → Repository → View → User.*

---

## 💡 Bảo mật & xử lý lỗi

- **Phân quyền rõ ràng:** Admin / User.  
- **Xác thực dữ liệu đầu vào:** tránh nhập sai, thiếu thông tin.  
- **Ghi log và rollback khi xảy ra lỗi.**  
- **Thông báo lỗi thân thiện:** hiển thị hướng dẫn cụ thể.  
- **Kết nối Cloud MySQL bảo mật bằng SSL.**

---

## 🧩 Các luồng chức năng tiêu biểu

### 🔸 Quản lý nhóm
1. Admin tạo nhóm (tên, mô tả, loại nhóm).  
2. Hệ thống sinh mã tham gia tự động.  
3. Gửi lời mời qua email hoặc QR code.  
4. Thành viên nhập mã để tham gia nhóm.  

### 🔸 Quản lý thu chi
1. Thêm, sửa, xóa giao dịch thu hoặc chi.  
2. Gán giao dịch cho nhóm, người thực hiện, danh mục.  
3. Tính toán tự động **tổng thu, tổng chi, số dư**.  
4. Phê duyệt giao dịch (Admin duyệt, thành viên gửi yêu cầu).  
5. Hiển thị danh sách giao dịch và chi tiết từng giao dịch.

### 🔸 Quản lý thông báo
1. Admin tạo giao dịch thu/chi.  
2. Hệ thống cập nhật số dư nhóm.  
3. Nếu cần, gửi yêu cầu phê duyệt đến Admin.  
4. Giao dịch được duyệt / từ chối → quỹ nhóm được cập nhật.  

### 🔸 Báo cáo và thống kê
- Tính **tổng thu**, **tổng chi**, **số dư**, hiển thị biểu đồ.  
- Cho phép lọc dữ liệu theo nhóm, thời gian, loại giao dịch.  

---

## 🧠 Lợi ích của lập trình hướng đối tượng (OOP)

- **Encapsulation:** Bảo vệ và kiểm soát dữ liệu.  
- **Inheritance:** Tái sử dụng và mở rộng dễ dàng.  
- **Polymorphism:** Linh hoạt trong xử lý nghiệp vụ.  
- **Abstraction:** Giảm độ phức tạp, dễ bảo trì.  
- **SOLID:** Cấu trúc rõ ràng, dễ mở rộng module.  

---

## ⚠️ Hạn chế hiện tại

- Giao diện chưa hỗ trợ **Dark Mode** và **đa ngôn ngữ**.  
- Thiếu xác thực nâng cao (JWT, 2FA).  
- Chưa tối ưu cho thiết bị di động.  
- Phụ thuộc vào kết nối mạng (Cloud Database).  

---

## 🚀 Định hướng phát triển

- Phát triển **ứng dụng di động (Flutter / React Native)**.  
- Tích hợp **AI** để phân tích và dự báo chi tiêu.  
- Bổ sung **JWT Authentication**, **2FA**, và **mã hóa dữ liệu**.  
- Nâng cấp hệ thống **real-time notification** bằng WebSocket / Firebase.  
- Bổ sung báo cáo nâng cao (xuất PDF/Excel).  
- Tối ưu **UI/UX**, hỗ trợ nhiều ngôn ngữ.  

---


