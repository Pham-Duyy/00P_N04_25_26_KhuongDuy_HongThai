package test;

public class testRole {
    public static void main(String[] args) {
        // Tạo vai trò
        model.Role role1 = new model.Role("Admin", "Manager");
        model.Role role2 = new model.Role("User", "Sinh Viên");
        
        // Hiển thị thông tin vai trò
        System.out.println("===== Thông tin vai trò =====");
        role1.displayInfo();
        System.out.println("-------------------");
        role2.displayInfo();
        
        // Cập nhật thông tin vai trò
        role1.setDescription("Manager - Cập nhật");
        role2.setRoleName("User - Cập nhật");
        
        System.out.println("\n===== Thông tin vai trò sau khi cập nhật =====");
        role1.displayInfo();
        System.out.println("-------------------");
        role2.displayInfo();
    }
}
