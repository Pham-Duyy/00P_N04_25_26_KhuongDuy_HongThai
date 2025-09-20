package model;
public class Fund{
    private String fundName;
    private double totalAmount;

    // Constructor
    public Fund(String fundName, double totalAmount) {
        this.fundName = fundName;
        this.totalAmount = totalAmount;
    }

    // Hiển thị thông tin quỹ
    public void displayInfo() {
        System.out.println("Fund Name: " + fundName);
        System.out.println("Total Amount: " + totalAmount);
    }

    // Thêm tiền vào quỹ
    public void addFunds(double amount) {
        if (amount > 0) {
            totalAmount += amount;
            System.out.println("Added " + amount + " to the fund. New total: " + totalAmount);
        } else {
            System.out.println("Amount must be positive.");
        }
    }

    // Rút tiền từ quỹ
    public void withdrawFunds(double amount) {
        if (amount > 0 && amount <= totalAmount) {
            totalAmount -= amount;
            System.out.println("Withdrew " + amount + " from the fund. New total: " + totalAmount);
        } else {
            System.out.println("Invalid withdrawal amount.");
        }
    }

    // Getter cho fundName
    public String getFundName() {
        return fundName;
    }

    // Setter cho fundName
    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    // Getter cho totalAmount
    public double getTotalAmount() {
        return totalAmount;
    }

    // Setter cho totalAmount
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}