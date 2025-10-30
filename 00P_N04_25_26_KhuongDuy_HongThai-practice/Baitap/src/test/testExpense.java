package test;

public class testExpense {
    public static void main(String[] args) {
        // Tạo các khoản chi tiêu
        model.Expense expense1 = new model.Expense("Liên hoan", 200.0);
        model.Expense expense2 = new model.Expense("Áo đặt", 150.0);
        
        // Hiển thị thông tin các khoản chi tiêu
        System.out.println("===== Thông tin các khoản chi tiêu =====");
        expense1.displayInfo();
        System.out.println("-------------------");
        expense2.displayInfo();
        
        // Cập nhật thông tin các khoản chi tiêu
        expense1.setAmount(250.0);
        expense2.setExpenseName("Áo đặt - Cập nhật");
        
        System.out.println("\n===== Thông tin các khoản chi tiêu sau khi cập nhật =====");
        expense1.displayInfo();
        System.out.println("-------------------");
        expense2.displayInfo();
    }
}
