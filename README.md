# 00P_N04_25_26_KhuongDuy_HongThai
Repo Working Group
NỘI DUNG 1
XÂY DỰNG ỨNG DỤNG QUẢN LÝ QUỸ NHÓM CỘNG ĐỒNG
I.  MỤC TIÊU ỨNG DỤNG
Ứng dụng hỗ trợ các nhóm cộng đồng như lớp học, nhóm bạn bè, nhóm thiện nguyện, câu lạc bộ... trong việc quản lý quỹ hoạt động, bao gồm:
Quản lý thành viên
Ghi nhận các khoản đóng góp và chi tiêu
Phân loại chi tiêu theo danh mục
Tính toán số dư quỹ
Thống kê, báo cáo và xuất file
Ứng dụng có giao diện web đơn giản, dễ sử dụng, chạy được trên trình duyệt với công nghệ Spring Boot.
II.  CÁC CHỨC NĂNG CHÍNH
1.  Quản lý Thành viên (Member)
Thêm, sửa, xoá thành viên
Liệt kê danh sách tất cả thành viên
Tìm kiếm và lọc theo tên hoặc vai trò
2.  Quản lý Khoản đóng góp (Contribution)
Thêm, sửa, xoá khoản đóng góp
Gán khoản đóng góp cho thành viên
Liệt kê theo thành viên hoặc theo tháng
Thống kê:
Thành viên đóng nhiều nhất
Thành viên đóng ít nhất
3.  Quản lý Khoản chi tiêu (Expense)
Thêm, sửa, xoá khoản chi tiêu
Liệt kê khoản chi theo ngày hoặc tháng
Gán khoản chi cho danh mục
4.  Quản lý Danh mục chi tiêu (Category)
Các danh mục mặc định:
Ăn uống
Đi chơi/Giải trí
Hoạt động tập thể
Người dùng có thể thêm danh mục mới
Dùng để phân loại khoản chi tiêu
5.  Tính năng nâng cao
Tính số dư quỹ:
Số dư = Tổng đóng góp - Tổng chi tiêu
Thống kê:
Chi tiêu theo tháng
Chi tiêu theo danh mục
Báo cáo cuối tháng:
Dạng bảng (trên giao diện hoặc xuất file)
(Tuỳ chọn) Biểu đồ thống kê bằng thư viện
III. YÊU CẦU VỀ DỮ LIỆU
Dữ liệu trong bộ nhớ:
Sử dụng các cấu trúc như: ArrayList, LinkedList, HashMap, Map, v.v.
Dữ liệu lưu trữ vĩnh viễn:
Được ghi xuống các file nhị phân có đuôi .dat
Mỗi loại dữ liệu có thể lưu riêng ra một file
