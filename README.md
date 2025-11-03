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

## 👨‍💻 Thành Viên Nhóm

| Họ và tên | MSV | Vai trò |
|------------|------|----------|
| **Phạm Khương Duy** | 23010743 | Trưởng nhóm – Backend, Database, Kiểm thử |
| **Dương Hồng Thái** | 23010326 | Frontend, UI/UX, Báo cáo, Demo |


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

### 👤 1. Quản Lý Người Dùng (User Management)

**Mô tả:**  
Chức năng cho phép người dùng đăng ký, đăng nhập và quản lý thông tin cá nhân.  
Hệ thống phân quyền rõ ràng giữa **Admin** và **User**, đảm bảo an toàn và kiểm soát truy cập hiệu quả.

**Thuộc tính chính (Entity: `User`)**
| Thuộc tính | Kiểu dữ liệu | Mô tả |
|-------------|---------------|-------|
| `id` | Long | Định danh duy nhất cho người dùng |
| `name` | String | Họ tên hiển thị |
| `email` | String | Email dùng để đăng nhập hoặc nhận thông báo |
| `username` | String | Tên đăng nhập duy nhất |
| `password` | String | Mật khẩu đã được mã hóa (BCrypt) |
| `role` | Enum | Vai trò người dùng: `ADMIN` hoặc `USER` |

**Chi tiết chức năng**
- 🔹 Đăng ký tài khoản: nhập thông tin cơ bản, kiểm tra trùng lặp email/username, mã hóa mật khẩu và lưu.  
- 🔹 Đăng nhập: xác thực thông tin người dùng, phân quyền truy cập.  
- 🔹 Cập nhật hồ sơ cá nhân: thay đổi tên, ảnh, email hoặc mật khẩu.  
- 🔹 Đổi mật khẩu có xác thực mật khẩu cũ.  
- 🔹 Quản lý phiên đăng nhập (token session, logout).  

---

### 👥 2. Quản Lý Nhóm (Group Management)

**Mô tả:**  
Chức năng cho phép người dùng tạo, chỉnh sửa, xóa nhóm; mời thành viên và quản lý các hoạt động trong nhóm.

**Thuộc tính chính (Entity: `Group`)**
| Thuộc tính | Kiểu dữ liệu | Mô tả |
|-------------|---------------|-------|
| `id` | Long | Định danh nhóm |
| `name` | String | Tên nhóm |
| `description` | String | Mô tả ngắn gọn về nhóm |
| `joinCode` | String | Mã tham gia duy nhất (6 ký tự) |
| `createdDate` | Date | Ngày tạo nhóm |
| `isActive` | Boolean | Trạng thái hoạt động |
| `createdBy` | String | Người tạo nhóm |
| `maxMembers` | Int | Số lượng thành viên tối đa |

**Chi tiết chức năng**
- 🔹 Tạo nhóm mới với tên, mô tả, loại nhóm (Public/Private).  
- 🔹 Gửi lời mời tham gia nhóm qua email hoặc mã joinCode.  
- 🔹 Phân quyền thành viên trong nhóm (Admin, Member).  
- 🔹 Theo dõi số lượng thành viên, số dư quỹ, hoạt động thu chi.  
- 🔹 Xóa nhóm (toàn bộ lời mời và dữ liệu liên quan cũng bị xóa).  

---

### 💰 3. Quản Lý Thu – Chi (Transaction Management)

**Mô tả:**  
Quản lý toàn bộ giao dịch tài chính trong nhóm, bao gồm thu nhập, chi tiêu và thống kê quỹ.

**Thuộc tính chính (Entity: `Transaction`)**
| Thuộc tính | Kiểu dữ liệu | Mô tả |
|-------------|---------------|-------|
| `id` | Long | Định danh giao dịch |
| `amount` | Double | Số tiền thu hoặc chi |
| `description` | String | Nội dung giao dịch |
| `type` | Enum | Loại giao dịch: `INCOME` hoặc `EXPENSE` |
| `date` | Date | Ngày tạo hoặc thực hiện giao dịch |
| `approved` | Boolean | Trạng thái phê duyệt |
| `category` | Category | Danh mục chi tiêu |
| `payer` | User | Người tạo giao dịch |
| `group` | Group | Nhóm liên kết giao dịch |

**Chi tiết chức năng**
- 🔹 **Create:** Tạo giao dịch mới (thu/chi) cho nhóm, tự động cập nhật số dư.  
- 🔹 **Read:** Hiển thị danh sách giao dịch, cho phép tìm kiếm, lọc theo loại và thời gian.  
- 🔹 **Update:** Chỉnh sửa nội dung hoặc số tiền giao dịch.  
- 🔹 **Delete:** Xóa giao dịch, hệ thống cập nhật lại thống kê quỹ.  
- 🔹 **Phê duyệt (Approve):** Admin xác nhận giao dịch hợp lệ trước khi cập nhật vào quỹ.  

---

### 🏷️ 4. Quản Lý Danh Mục (Category Management)

**Mô tả:**  
Phân loại các giao dịch thành các nhóm danh mục để dễ thống kê và báo cáo.

**Thuộc tính chính (Entity: `Category`)**
| Thuộc tính | Kiểu dữ liệu | Mô tả |
|-------------|---------------|-------|
| `id` | Long | Định danh danh mục |
| `name` | String | Tên danh mục |
| `description` | String | Mô tả ngắn gọn |
| `type` | Enum | Loại: `INCOME` hoặc `EXPENSE` |

**Chi tiết chức năng**
- 🔹 Tạo danh mục thu nhập hoặc chi tiêu mới.  
- 🔹 Sửa tên hoặc mô tả danh mục.  
- 🔹 Gắn danh mục vào từng giao dịch.  
- 🔹 Kiểm tra ràng buộc: không cho xóa danh mục nếu đang có giao dịch sử dụng.  

---

### 📊 5. Báo Cáo & Thống Kê (Reports & Analytics)

**Mô tả:**  
Tổng hợp và hiển thị báo cáo tài chính chi tiết theo nhóm, người dùng, loại giao dịch và thời gian.

**Thuộc tính dữ liệu hiển thị**
| Thuộc tính | Mô tả |
|-------------|-------|
| `totalIncome` | Tổng thu nhập của nhóm |
| `totalExpense` | Tổng chi tiêu của nhóm |
| `balance` | Số dư hiện tại |
| `transactionsByCategory` | Danh sách giao dịch được phân loại |
| `transactionsByDate` | Giao dịch theo ngày hoặc tháng |

**Chi tiết chức năng**
- 🔹 Biểu đồ trực quan (Line, Bar, Pie) thể hiện thu – chi.  
- 🔹 Bộ lọc theo **nhóm**, **thời gian**, **thành viên**, **loại giao dịch**.  
- 🔹 Xuất báo cáo thống kê ra định dạng PDF hoặc Excel (định hướng phát triển).  
- 🔹 Hiển thị thông báo cảnh báo khi chi tiêu vượt mức thu nhập.  

---

### 📧 6. Lời Mời & Thông Báo (Invitations & Notifications)

**Mô tả:**  
Quản lý quy trình mời thành viên mới vào nhóm và thông báo các sự kiện quan trọng.

**Thuộc tính chính (Entity: `Invitation`, `Notification`)**
| Thuộc tính | Kiểu dữ liệu | Mô tả |
|-------------|---------------|-------|
| `id` | Long | Định danh lời mời/thông báo |
| `email` | String | Email người được mời |
| `group` | Group | Nhóm gửi lời mời |
| `status` | Enum | Trạng thái: `PENDING`, `ACCEPTED`, `DECLINED` |
| `createdDate` | Date | Ngày gửi lời mời/thông báo |

**Chi tiết chức năng**
- 🔹 Gửi lời mời qua email hoặc mã nhóm (Join Code).  
- 🔹 Người nhận chấp nhận hoặc từ chối lời mời.  
- 🔹 Gửi thông báo khi có giao dịch mới, thay đổi thành viên hoặc phê duyệt giao dịch.  
- 🔹 (Định hướng) Hỗ trợ **real-time notification** bằng WebSocket/Firebase.  

---

### 🧪 7. Kiểm Thử, Bảo Mật & Xử Lý Lỗi

**Mô tả:**  
Đảm bảo hệ thống hoạt động ổn định, dữ liệu an toàn và xử lý lỗi hiệu quả.

**Chi tiết chức năng**
- 🔹 Kiểm thử logic nghiệp vụ bằng **JUnit**, **MockMVC**.  
- 🔹 Ghi log hoạt động và lỗi trong hệ thống.  
- 🔹 Kiểm tra dữ liệu đầu vào (front-end & back-end validation).  
- 🔹 Phân quyền truy cập bằng **Spring Security**.  
- 🔹 Xử lý lỗi kết nối cơ sở dữ liệu, rollback giao dịch khi thất bại.  

---

📘 *Tất cả các chức năng trên đều được tổ chức theo nguyên lý SOLID, đảm bảo khả năng mở rộng, dễ bảo trì và nâng cấp hệ thống trong tương lai.*

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


