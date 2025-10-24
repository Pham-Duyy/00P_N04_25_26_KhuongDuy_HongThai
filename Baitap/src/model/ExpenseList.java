package model;

public class ExpenseList {
    private Expense[] expenses;
    private int count;

    // Constructor
    public ExpenseList(int capacity) {
        expenses = new Expense[capacity];
        count = 0;
    }

    // Thêm chi tiêu mới vào danh sách
    public void addExpense(Expense expense) {
        if (count < expenses.length) {
            expenses[count++] = expense;
        } else {
            System.out.println("Expense list is full!");
        }
    }

    // Hiển thị tất cả các chi tiêu trong danh sách
    public void displayAllExpenses() {
        if (count == 0) {
            System.out.println("Danh sách chi tiêu trống!");
            return;
        }
        for (int i = 0; i < count; i++) {
            expenses[i].displayInfo();
            System.out.println("-------------------");
        }
    }

    // Tìm kiếm chi tiêu theo tên
    public Expense findExpenseByName(String expenseName) {
        for (int i = 0; i < count; i++) {
            if (expenses[i].getExpenseName().equalsIgnoreCase(expenseName)) {
                return expenses[i];
            }
        }
        return null; // Không tìm thấy chi tiêu
    }

    // Xóa chi tiêu theo tên
    public boolean removeExpense(String expenseName) {
        for (int i = 0; i < count; i++) {
            if (expenses[i].getExpenseName().equalsIgnoreCase(expenseName)) {
                // Dịch chuyển phần tử để lấp chỗ trống
                for (int j = i; j < count - 1; j++) {
                    expenses[j] = expenses[j + 1];
                }
                expenses[--count] = null;
                return true;
            }
        }
        return false;
    }

    // Cập nhật chi tiêu theo tên
    public boolean updateExpense(String oldName, String newName, double newAmount) {
        Expense e = findExpenseByName(oldName);
        if (e != null) {
            e.updateExpense(newName, newAmount); // ✅ dùng hàm update trong Expense
            return true;
        }
        return false;
    }
}
