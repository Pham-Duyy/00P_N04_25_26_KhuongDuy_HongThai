package Test;

import Code.User;
import java.util.Scanner;

public class TestUser {
    public static void main(String[] args) {
        runTest();
    }

    public static void runTest() {
        Scanner scanner = new Scanner(System.in);

        // Khởi tạo User
        User user = new User("phamDuy", "pDuy2@email.com", "123456");
        System.out.println("Đang chạy test user!");

        // In thông tin user
        System.out.println("===== Thông tin User ban đầu =====");
        System.out.println("Tên người dùng: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Mật khẩu: " + user.getPassword());
        System.out.println("==================================");

        // Đổi mật khẩu bằng forgotPassword
        user.forgotPassword("newpassword123");

        // Kiểm tra lại mật khẩu mới
        System.out.println("\nMật khẩu mới: " + user.getPassword());

        // Thay đổi email
        user.setemail("vana.new@email.com");
        System.out.println("Email mới: " + user.getEmail());

        // Nhập tên người dùng mới
        System.out.print("\nNhập tên người dùng mới: ");
        String newUsername = scanner.nextLine();
        user.setUsername(newUsername);

        // Nhập email mới
        System.out.print("Nhập email mới: ");
        String newEmail = scanner.nextLine();
        user.setemail(newEmail);

        // Nhập mật khẩu mới
        System.out.print("Nhập mật khẩu mới: ");
        String newPassword = scanner.nextLine();
        user.forgotPassword(newPassword);

        // Hiển thị thông tin mới
        System.out.println("\n===== Thông tin User sau khi cập nhật =====");
        System.out.println("Tên người dùng mới: " + user.getUsername());
        System.out.println("Email mới: " + user.getEmail());
        System.out.println("Mật khẩu mới: " + user.getPassword());
        System.out.println("===========================================");

        scanner.close();
    }
}