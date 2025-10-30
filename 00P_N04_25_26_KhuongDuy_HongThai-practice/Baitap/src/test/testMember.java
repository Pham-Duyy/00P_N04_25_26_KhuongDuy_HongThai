package test;

public class testMember {
    public static void main(String[] args) {
        // Tạo member
        model.Member member1 = new model.Member("Le Van C", "M001", "levanc@example.com", "0123456789", "Ha Noi", "2023-01-01", "Gold");
        model.Member member2 = new model.Member("Pham Thi D", "M002", "phamthid@example.com", "0987654321", "Ho Chi Minh", "2023-02-01", "Silver");
        // Hiển thị thông tin
        System.out.println("===== Thông tin thành viên =====");
        member1.displayInfo();
        System.out.println("-------------------");
        member2.displayInfo();
        // Cập nhật thông tin
        member1.setName("Le Van C Updated");
        member2.setId("M002-Updated");
        System.out.println("\n===== Thông tin thành viên sau khi cập nhật =====");
    }
}

