package model;

public class Expense {
    private String expenseName;
    private double amount;

    // Constructor
    public Expense(String expenseName, double amount) {
        this.expenseName = expenseName;
        this.amount = amount;
    }

    // Hiển thị thông tin chi tiêu
    public void displayInfo() {
        System.out.println("Expense Name: " + expenseName);
        System.out.println("Amount: " + amount);
    }

    // Getter cho expenseName
    public String getExpenseName() {
        return expenseName;
    }

    // Setter cho expenseName
    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    // Getter cho amount
    public double getAmount() {
        return amount;
    }

    // Setter cho amount
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // ✅ Hàm cập nhật trực tiếp
    public void updateExpense(String newName, double newAmount) {
        this.expenseName = newName;
        this.amount = newAmount;
    }
}
