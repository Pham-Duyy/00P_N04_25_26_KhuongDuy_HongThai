package test;

public class testIncome {
    public static void main(String[] args) {
        // Tạo các khoản thu nhập
        model.Income income1 = new model.Income("Liên Hoan", 500.0);
        model.Income income2 = new model.Income("Áo đội", 300.0);
        
        // Hiển thị thông tin các khoản thu nhập
        System.out.println("===== Thông tin các khoản thu nhập =====");
        income1.displayInfo();
        System.out.println("-------------------");
        income2.displayInfo();
        
        // Cập nhật thông tin các khoản thu nhập
        income1.setAmount(550.0);
        income2.setIncomeName("Áo đội - Cập nhật");
        
        System.out.println("\n===== Thông tin các khoản thu nhập sau khi cập nhật =====");
        income1.displayInfo();
        System.out.println("-------------------");
        income2.displayInfo();
    }
}
