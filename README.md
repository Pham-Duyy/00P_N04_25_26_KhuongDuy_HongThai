# 💰 Fund Manager – Ứng Dụng Quản Lý Ngân Sách Nhóm  
### 📚 Mã lớp: 00P_N04_25_26_KhuongDuy_HongThai  

---

## 1️⃣ Giới thiệu  
**Fund Manager** là ứng dụng web giúp người dùng quản lý thu chi nhóm một cách **minh bạch, tiện lợi và hiệu quả**.  
Ứng dụng cho phép **tạo nhóm, gửi lời mời, ghi nhận giao dịch, thống kê chi tiêu, và theo dõi quỹ nhóm theo thời gian thực.**

---

## 2️⃣ Tính năng chính  

### 👑 Dành cho Admin  
- Tạo và quản lý nhóm, gửi lời mời thành viên.  
- Quản lý giao dịch thu/chi, phê duyệt hoặc từ chối giao dịch.  
- Theo dõi báo cáo tổng hợp, thống kê và biểu đồ tài chính.  
- Quản lý thành viên và phân quyền người dùng.  

### 👥 Dành cho User  
- Đăng ký, đăng nhập, tham gia nhóm qua mã hoặc lời mời.  
- Theo dõi giao dịch, đóng góp, và xem lịch sử thu chi cá nhân.  
- Xem báo cáo tổng quan và quản lý thông tin cá nhân.  

---

## 3️⃣ Kiến trúc hệ thống  

Ứng dụng được thiết kế theo mô hình **MVC (Model – View – Controller)** giúp tách biệt giao diện, logic và dữ liệu.  

---

## 4️⃣ Thành viên nhóm  

| Họ và tên | MSV | Vai trò |
|------------|------|----------|
| **Phạm Khương Duy** | 23010743 | Trưởng nhóm – Frontend, Backend, Database, Kiểm thử |
| **Dương Hồng Thái** | 23010326 | Kiểm thử, UI/UX, Báo cáo, Demo |

---

## 5️⃣ Công nghệ sử dụng  

| Công nghệ / Công cụ | Vai trò |
|----------------------|---------|
| **Spring Boot 3** | Xây dựng backend, xử lý logic nghiệp vụ |
| **Thymeleaf** | Template engine render HTML |
| **Bootstrap 5** | Thiết kế giao diện người dùng |
| **MySQL (Aiven Cloud)** | Lưu trữ dữ liệu |
| **Spring Data JPA (Hibernate)** | ORM mapping |
| **Git & GitHub** | Quản lý mã nguồn |
| **draw.io / Lucidchart** | Vẽ sơ đồ UML |

---

## 6️⃣ Cấu trúc thư mục dự án  

```bash
project-root/
│
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com/oop/quanlyngansach/
│  │  │     ├─ controller/
│  │  │     │   ├─ AdminController.java
│  │  │     │   ├─ AuthController.java
│  │  │     │   ├─ GroupAdminController.java
│  │  │     │   ├─ GroupUserController.java
│  │  │     │   ├─ PersonalFinanceController.java
│  │  │     │   ├─ ReportController.java
│  │  │     │   ├─ UserController.java
│  │  │     │   └─ UserTransactionController.java
│  │  │     │
│  │  │     ├─ model/
│  │  │     │   ├─ Group.java
│  │  │     │   ├─ GroupInvite.java
│  │  │     │   ├─ Member.java
│  │  │     │   ├─ Report.java
│  │  │     │   ├─ Transaction.java
│  │  │     │   ├─ TransactionParticipant.java
│  │  │     │   └─ User.java
│  │  │     │
│  │  │     ├─ repository/
│  │  │     │   ├─ GroupInviteRepository.java
│  │  │     │   ├─ GroupRepository.java
│  │  │     │   ├─ TransactionParticipantRepository.java
│  │  │     │   ├─ TransactionRepository.java
│  │  │     │   └─ UserRepository.java
│  │  │     │
│  │  │     ├─ service/
│  │  │     │   ├─ GroupInviteService.java
│  │  │     │   ├─ GroupService.java
│  │  │     │   ├─ GroupServiceImpl.java
│  │  │     │   ├─ ReportService.java
│  │  │     │   ├─ ReportServiceImpl.java
│  │  │     │   ├─ TransactionParticipantService.java
│  │  │     │   ├─ TransactionService.java
│  │  │     │   ├─ TransactionServiceImpl.java
│  │  │     │   ├─ UserService.java
│  │  │     │   └─ UserServiceImpl.java
│  │  │     │
│  │  │     └─ Main.java
│  │  │
│  │  └─ resources/
│  │     ├─ static/img/
│  │     │   └─ anh QR.jpg
│  │     └─ templates/
│  │         ├─ admin/
│  │         │   ├─ auth/
│  │         │   │   ├─ login.html
│  │         │   │   └─ register.html
│  │         │   ├─ finance/
│  │         │   │   ├─ transaction-detail.html
│  │         │   │   └─ transactions.html
│  │         │   ├─ groups/
│  │         │   │   ├─ group-create.html
│  │         │   │   └─ group-detail.html
│  │         │   └─ reports/
│  │         │       ├─ dashboard.html
│  │         │       ├─ index.html
│  │         │       ├─ users.html
│  │         │       └─ contributions.html
│  │         └─ user/
│  │             ├─ finance/
│  │             │   └─ transactions.html
│  │             ├─ groups/
│  │             │   ├─ group-detail.html
│  │             │   ├─ invites.html
│  │             │   └─ my-groups.html
│  │             └─ personal-finance/
│  │                 ├─ dashboard.html
│  │                 └─ index.html
│  │
│  └─ test/java/com/oop/quanlyngansach/
│      ├─ AdminControllerTest.java
│      ├─ AuthControllerTest.java
│      ├─ DemoApplicationTests.java
│      ├─ GroupAdminControllerTest.java
│      ├─ GroupUserControllerTest.java
│      └─ UserControllerTest.java
│
├─ .gitignore
├─ .gitattributes
├─ mvnw
├─ mvnw.cmd
└─ pom.xml
```

#7️⃣ Mô tả các chức năng chính
📂 Quản lý nhóm

Tạo nhóm, đặt tên và mô tả.

Xóa nhóm (chỉ admin có quyền).

Xem danh sách nhóm đã tham gia.

💵 Quản lý giao dịch

Thêm, sửa, xóa, xem chi tiết giao dịch.

Tự động tính tổng chi, thu của từng nhóm.

Hiển thị biểu đồ chi tiêu theo loại.

✉️ Mời và quản lý thành viên

Gửi lời mời qua GroupInvite.

Thành viên có thể chấp nhận hoặc từ chối lời mời.

Admin có thể xóa thành viên khỏi nhóm.

📊 Báo cáo tài chính

Thống kê chi tiêu theo thành viên, loại giao dịch và thời gian.

Hiển thị tổng đóng góp và phần chi tiêu từng thành viên.

🧭 Sơ đồ UML & Kiến trúc hệ thống
🎯 Use Case Diagram
<img src="https://github.com/user-attachments/assets/7bd82403-e56c-480e-bace-b03883649d63" width="600" />
🧩 Class Diagram
<img src="https://github.com/user-attachments/assets/0d4a267b-a8fd-44d3-b9b6-ea3f944ae4ec" width="800" />
🔁 Sequence Diagram
CRUD cho User
<img src="https://github.com/user-attachments/assets/ec888897-a68d-434a-9212-b47003e8958d" width="700" />
CRUD cho GroupAdmin
<img src="https://github.com/user-attachments/assets/8e4d5a96-87f8-4ec8-9306-7072968835cf" width="800" />
CRUD cho Transaction
<img src="https://github.com/user-attachments/assets/c116881a-5e95-4cf2-8572-46e3ba097461" width="1000" />
⚙️ Cách chạy dự án

1️1️⃣ Clone repository:

git clone https://github.com/Pham-Duyy/00P_N04_25_26_KhuongDuy_HongThai.git


2️⃣ Mở dự án bằng IDE (IntelliJ / Eclipse)
3️⃣ Cấu hình database trong application.properties
4️⃣ Chạy dự án:

mvn spring-boot:run

5️⃣ Truy cập trình duyệt:
👉 http://localhost:8080

🚧 Hạn chế & Định hướng phát triển
⚠️ Hạn chế

Giao diện chưa hỗ trợ Dark Mode và đa ngôn ngữ.

Chưa có JWT Authentication hoặc 2FA.

Phân quyền còn đơn giản (Admin, User).

🌱 Định hướng

Phát triển ứng dụng Mobile (Flutter/React Native).

Ứng dụng AI để dự báo chi tiêu và tối ưu ngân sách.

Cải tiến UI/UX, bổ sung Dashboard thông minh.
