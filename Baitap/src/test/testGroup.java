package test;

public class testGroup {
    public static void main(String[] args) {
        // Tạo các nhóm
        model.Group group1 = new model.Group("Nhóm A", "Nhóm bóng đá");
        model.Group group2 = new model.Group("Nhóm B", "Nhóm văn nghệ");
        
        // Hiển thị thông tin các nhóm
        System.out.println("===== Thông tin các nhóm =====");
        group1.displayInfo();
        System.out.println("-------------------");
        group2.displayInfo();
        
        // Cập nhật thông tin các nhóm
        group1.setDescription("Nhóm bóng đá - Cập nhật");
        group2.setGroupName("Nhóm B - Cập nhật");
        
        System.out.println("\n===== Thông tin các nhóm sau khi cập nhật =====");
        group1.displayInfo();
        System.out.println("-------------------");
        group2.displayInfo();
    }
}
