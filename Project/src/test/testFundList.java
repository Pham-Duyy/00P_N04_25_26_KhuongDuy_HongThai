package test;

import model.Fund;
import model.FundList;
import java.util.Scanner;

public class testFundList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập sức chứa tối đa cho danh sách quỹ: ");
        int capacity = sc.nextInt();
        sc.nextLine();

        FundList fundList = new FundList(capacity);

        int choice;
        do {
            System.out.println("\n=== QUẢN LÝ QUỸ ===");
            System.out.println("1. Thêm quỹ");
            System.out.println("2. Hiển thị danh sách quỹ");
            System.out.println("3. Tìm quỹ theo tên");
            System.out.println("4. Xóa quỹ");
            System.out.println("5. Cập nhật quỹ");
            System.out.println("6. Nạp tiền vào quỹ");
            System.out.println("7. Rút tiền từ quỹ");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Nhập tên quỹ: ");
                    String name = sc.nextLine();
                    System.out.print("Nhập tổng số tiền ban đầu: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    fundList.addFund(new Fund(name, amount));
                    break;

                case 2:
                    fundList.displayAllFunds();
                    break;

                case 3:
                    System.out.print("Nhập tên quỹ cần tìm: ");
                    String search = sc.nextLine();
                    Fund f = fundList.findFundByName(search);
                    if (f != null) {
                        f.displayInfo();
                    } else {
                        System.out.println("Không tìm thấy quỹ!");
                    }
                    break;

                case 4:
                    System.out.print("Nhập tên quỹ cần xóa: ");
                    String delName = sc.nextLine();
                    if (fundList.removeFund(delName)) {
                        System.out.println("Đã xóa thành công!");
                    } else {
                        System.out.println("Không tìm thấy quỹ để xóa!");
                    }
                    break;

                case 5:
                    System.out.print("Nhập tên quỹ cần cập nhật: ");
                    String oldName = sc.nextLine();
                    System.out.print("Tên mới: ");
                    String newName = sc.nextLine();
                    System.out.print("Tổng số tiền mới: ");
                    double newAmount = sc.nextDouble();
                    sc.nextLine();
                    if (fundList.updateFund(oldName, newName, newAmount)) {
                        System.out.println("Cập nhật thành công!");
                    } else {
                        System.out.println("Không tìm thấy quỹ để cập nhật!");
                    }
                    break;

                case 6:
                    System.out.print("Nhập tên quỹ cần nạp tiền: ");
                    String fundDeposit = sc.nextLine();
                    Fund depositFund = fundList.findFundByName(fundDeposit);
                    if (depositFund != null) {
                        System.out.print("Nhập số tiền cần nạp: ");
                        double deposit = sc.nextDouble();
                        sc.nextLine();
                        depositFund.addFunds(deposit);
                    } else {
                        System.out.println("Không tìm thấy quỹ!");
                    }
                    break;

                case 7:
                    System.out.print("Nhập tên quỹ cần rút tiền: ");
                    String fundWithdraw = sc.nextLine();
                    Fund withdrawFund = fundList.findFundByName(fundWithdraw);
                    if (withdrawFund != null) {
                        System.out.print("Nhập số tiền cần rút: ");
                        double withdraw = sc.nextDouble();
                        sc.nextLine();
                        withdrawFund.withdrawFunds(withdraw);
                    } else {
                        System.out.println("Không tìm thấy quỹ!");
                    }
                    break;

                case 0:
                    System.out.println("Thoát chương trình!");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);

        sc.close();
    }
}
