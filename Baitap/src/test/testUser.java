package test;
import model.User;

public class testUser {
    public static void main(String[] args) {
        // Tạo user
        User user1 = new User(1, "Nguyen Van A", "a@example.com", "123456");
        User user2 = new User(2, "Tran Thi B", "b@example.com", "abcdef");

        // Hiển thị thông tin
        System.out.println("===== Thông tin người dùng =====");
        user1.displayInfo();
        System.out.println("-------------------");
        user2.displayInfo();

        // Thử đăng nhập
        System.out.println("\n===== Thử đăng nhập =====");
        if (user1.login("a@example.com", "123456")) {
            System.out.println("Đăng nhập thành công: " + user1.getName());
        } else {
            System.out.println("Đăng nhập thất bại!");
        }

        // Đăng xuất
        System.out.println("\n===== Đăng xuất =====");
        user1.logout();
    }
    
}
