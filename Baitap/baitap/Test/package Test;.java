package Test; 
// Khai báo file này thuộc package Test

import Code.User; 
// Import class User từ package Code

import java.util.Scanner; 
// Import lớp Scanner để nhập dữ liệu từ bàn phím

// Test class for User
public class TestUser { 
    // Định nghĩa class TestUser

    public static void main(String[] args) { 
        // Hàm main, là điểm bắt đầu của chương trình
        runTest(); 
        // Gọi phương thức runTest để thực hiện các thao tác kiểm thử
    }

    // Method to run the test
    public static void runTest() { 
        // Định nghĩa phương thức runTest

        Scanner scanner = new Scanner(System.in); 
        // Tạo đối tượng Scanner để đọc dữ liệu nhập từ bàn phím

        // Khởi tạo User
        User user = new User("phamDuy", "pDuy2@email.com", "123456"); 
        // Tạo một đối tượng User với tên, email, mật khẩu mặc định

        System.out.println("Đang chạy test user!"); 
        // In ra thông báo bắt đầu test

        // In thông tin user
        System.out.println("===== Thông tin User ban đầu =====");
        // In dòng phân cách
        System.out.println("Tên người dùng: " + user.getUsername());
        // In tên người dùng hiện tại
        System.out.println("Email: " + user.getEmail());
        // In email hiện tại
        System.out.println("Mật khẩu: " + user.getPassword());
        // In mật khẩu hiện tại
        System.out.println("==================================");
        // In dòng phân cách

        // Đổi mật khẩu bằng forgotPassword
        user.forgotPassword("newpassword123");
        // Gọi phương thức forgotPassword để đổi mật khẩu thành "newpassword123"

        // Kiểm tra lại mật khẩu mới
        System.out.println("\nMật khẩu mới: " + user.getPassword());
        // In ra mật khẩu mới sau khi đổi

        // Thay đổi email
        user.setEmail("vana.new@email.com");
        // Gọi phương thức setEmail để đổi email thành "vana.new@email.com"
        System.out.println("Email mới: " + user.getEmail());
        // In ra email mới

        // Nhập tên người dùng mới
        System.out.print("\nNhập tên người dùng mới: ");
        // Yêu cầu người dùng nhập tên mới
        String newUsername = scanner.nextLine();
        // Đọc tên người dùng mới từ bàn phím
        user.setUsername(newUsername);
        // Cập nhật tên người dùng mới

        // Nhập email mới
        System.out.print("Nhập email mới: ");
        // Yêu cầu người dùng nhập email mới
        String newEmail = scanner.nextLine();
        // Đọc email mới từ bàn phím
        user.setEmail(newEmail);
        // Cập nhật email mới

        // Nhập mật khẩu mới
        System.out.print("Nhập mật khẩu mới: ");
        // Yêu cầu người dùng nhập mật khẩu mới
        String newPassword = scanner.nextLine();
        // Đọc mật khẩu mới từ bàn phím
        user.forgotPassword(newPassword);
        // Đặt lại mật khẩu mới

        // Hiển thị thông tin mới
        System.out.println("\n===== Thông tin User sau khi cập nhật =====");
        // In dòng phân cách
        System.out.println("Tên người dùng mới: " + user.getUsername());
        // In tên người dùng mới
        System.out.println("Email mới: " + user.getEmail());
        // In email mới
        System.out.println("Mật khẩu mới: " + user.getPassword());
        // In mật khẩu mới
        System.out.println("===========================================");
        // In dòng phân cách

        scanner.close();
        // Đóng đối tượng Scanner để giải phóng tài nguyên
    }
}