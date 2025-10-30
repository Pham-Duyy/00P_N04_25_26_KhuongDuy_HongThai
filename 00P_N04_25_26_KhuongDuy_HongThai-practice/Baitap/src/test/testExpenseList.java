package test;

import model.Expense;
import model.ExpenseList;
import java.util.Scanner;

public class testExpenseList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập sức chứa tối đa cho danh sách chi tiêu: ");
        int capacity = sc.nextInt();
        sc.nextLine(); // clear buffer

        ExpenseList list = new ExpenseList(capacity);

        int choice;
        do {
            System.out.println("\n=== QUẢN LÝ CHI TIÊU ===");
            System.out.println("1. Thêm chi tiêu");
            System.out.println("2. Hiển thị danh sách");
            System.out.println("3. Tìm chi tiêu theo tên");
            System.out.println("4. Xóa chi tiêu");
            System.out.println("5. Cập nhật chi tiêu");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Nhập tên chi tiêu: ");
                    String name = sc.nextLine();
                    System.out.print("Nhập số tiền: ");
                    double amount = sc.nextDouble();
                    sc.nextLine(); // clear buffer
                    list.addExpense(new Expense(name, amount));
                    break;

                case 2:
                    list.displayAllExpenses();
                    break;

                case 3:
                    System.out.print("Nhập tên chi tiêu cần tìm: ");
                    String search = sc.nextLine();
                    Expense found = list.findExpenseByName(search);
                    if (found != null) {
                        System.out.println("Đã tìm thấy:");
                        found.displayInfo();
                    } else {
                        System.out.println("Không tìm thấy chi tiêu!");
                    }
                    break;

                case 4:
                    System.out.print("Nhập tên chi tiêu cần xóa: ");
                    String delName = sc.nextLine();
                    if (list.removeExpense(delName)) {
                        System.out.println("Đã xóa thành công!");
                    } else {
                        System.out.println("Không tìm thấy chi tiêu để xóa!");
                    }
                    break;

                case 5:
                    System.out.print("Nhập tên chi tiêu cần cập nhật: ");
                    String oldName = sc.nextLine();
                    System.out.print("Tên mới: ");
                    String newName = sc.nextLine();
                    System.out.print("Số tiền mới: ");
                    double newAmount = sc.nextDouble();
                    sc.nextLine(); // clear buffer
                    if (list.updateExpense(oldName, newName, newAmount)) {
                        System.out.println("Cập nhật thành công!");
                    } else {
                        System.out.println("Không tìm thấy chi tiêu để cập nhật!");
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
