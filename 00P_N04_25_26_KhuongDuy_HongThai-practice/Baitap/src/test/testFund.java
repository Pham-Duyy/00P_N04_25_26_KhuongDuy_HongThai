package test;

public class testFund {
    public static void main(String[] args) {
        // Tạo quỹ
        model.Fund fund = new model.Fund("Quỹ bóng đá", 1000.0);
        
        // Hiển thị thông tin quỹ
        System.out.println("===== Thông tin quỹ =====");
        fund.displayInfo();
        
        // Thêm tiền vào quỹ
        System.out.println("\n===== Thêm tiền vào quỹ =====");
        fund.addFunds(500.0);
        
        // Rút tiền từ quỹ
        System.out.println("\n===== Rút tiền từ quỹ =====");
        fund.withdrawFunds(300.0);
        
        // Hiển thị thông tin quỹ sau các thao tác
        System.out.println("\n===== Thông tin quỹ sau các thao tác =====");
        fund.displayInfo();
    }
}
