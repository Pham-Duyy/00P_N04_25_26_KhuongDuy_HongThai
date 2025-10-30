00P_N04_25_26_KhuongDuy_HongThai
Repo Working Group

NỘI DUNG 1
XÂY DỰNG ỨNG DỤNG QUẢN LÝ NGÂN SÁCH NHÓM
I. MỤC TIÊU ỨNG DỤNG

Ứng dụng hỗ trợ các nhóm cộng đồng như lớp học, nhóm bạn bè, nhóm thiện nguyện, câu lạc bộ... trong việc quản lý quỹ hoạt động, bao gồm:
Quản lý thành viên
Ghi nhận các khoản đóng góp và chi tiêu
Phân loại chi tiêu theo danh mục
Tính toán số dư quỹ
Thống kê, báo cáo và xuất file
Ứng dụng có giao diện web đơn giản, dễ sử dụng, chạy được trên trình duyệt với công nghệ Spring Boot.
II. CÁC CHỨC NĂNG CHÍNH

Quản lý Thành viên (Member)
Thêm, sửa, xoá thành viên
kê danh sách tất cả thành viên
kiếm và lọc theo tên hoặc vai trò
Quản lý Khoản đóng góp (Contribution)
Sửa, xoá khoản đóng góp
Gán khoản đóng góp cho thành viên
Thống kê theo thành viên hoặc theo tháng Thống kê:
Thành viên đóng nhiều nhất
Thành viên đóng ít nhất
Quản lý Khoản chi tiêu (Expense)
Thêm, sửa, xoá khoản chi tiêu
Liệt kê khoản chi theo ngày hoặc tháng
Gán khoản chi cho danh mục
Quản lý Danh mục chi tiêu (Category)
Các danh mục mặc định:
Ăn uống
Đi chơi/Giải trí
Hoạt động tập thể
Người dùng có thể thêm danh mục mới
Dùng để phân loại khoản chi tiêu
Tính năng nâng cao
Tính số dư quỹ:
Số dư = Tổng đóng góp - Tổng chi tiêu Thống kê:
Chi tiêu theo tháng
Chi tiêu theo danh mục
Báo cáo cuối tháng:
Dạng bảng (trên giao diện hoặc xuất file)
III. YÊU CẦU VỀ DỮ LIỆU

Dữ liệu trong bộ nhớ:
Sử dụng các cấu trúc như: ArrayList, LinkedList, HashMap, Map, v.v.
Dữ liệu lưu trữ vĩnh viễn:
Được ghi xuống các file nhị phân có đuôi .dat
Mỗi loại dữ liệu có thể lưu riêng ra một file
SƠ ĐÔ KHỐI YÊU CẦU
1.1 Sơ đồ lớp UML
af9006db-f208-4a1d-8eb0-3a2ef9275470

1.2 1.2 Sơ đồ trình tự UML
291e4296-14f3-4501-a959-2889b4b7acf5

CÁC ĐỐI TƯỢNG CHÍNH ĐỂ MÔ TẢ ỨNG DỤNG
1. User (Người dùng)
Vai trò của User:
Là lớp cơ sở (base class) đại diện cho tất cả các loại người dùng trong hệ thống.
Quản lý thông tin chung về người dùng như tài khoản, quyền truy cập.
Cung cấp các hành động cơ bản mà tất cả người dùng đều thực hiện được, như đăng nhập và đăng xuất.
Chức năng chính của User:
login() Cho phép người dùng đăng nhập vào hệ thống bằng tài khoản và mật khẩu.
logout() Cho phép người dùng đăng xuất khỏi hệ thống.
Thuộc tính chung Lưu trữ thông tin cơ bản: userId, username, password, role (vai trò).
2. Admin (Quản trị viên)
Vai trò:
Là người quản lý hệ thống, chịu trách nhiệm quản lý người dùng và phân quyền cho các vai trò khác như Treasurer (Thủ quỹ) hoặc Member (Thành viên).
Chức năng chính:
addUser(user: User) – Thêm người dùng mới vào hệ thống.
removeUser(userId: int) – Xóa người dùng khỏi hệ thống.
assignRole(userId: int, role: String) – Gán hoặc thay đổi vai trò của người dùng (ví dụ: từ Member thành Treasurer).
3. Treasurer (Thủ quỹ)
Vai trò:
Là người phụ trách tài chính của tổ chức. Có quyền kiểm soát các khoản chi tiêu và lập báo cáo tài chính.
Chức năng chính:
approveExpense(expenseId: int) – Phê duyệt các khoản chi tiêu trước khi được xử lý.
generateReport() – Tạo báo cáo tài chính dựa trên các giao dịch thu – chi.
4. Member (Thành viên)
Vai trò:
Là thành viên của tổ chức, người có thể đóng góp tiền vào quỹ và theo dõi tình hình tài chính.
Chức năng chính:
makeContribution(amount: double) – Góp tiền vào quỹ chung.
viewBalance() – Xem số dư hiện tại của quỹ.
5. Fund (Quỹ tài chính)
Vai trò:
Là đối tượng trung tâm của hệ thống, nơi lưu trữ và quản lý số dư tiền mặt. Mọi giao dịch đều ảnh hưởng đến quỹ này.
Chức năng chính:
addContribution(contribution: Contribution) – Thêm đóng góp vào quỹ (do Member thực hiện).
processExpense(expense: Expense) – Trừ tiền quỹ khi có chi tiêu được phê duyệt.
getBalance() – Trả về số dư hiện tại của quỹ
