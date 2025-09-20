package model;

public class Income {
    private String incomeName;
    private double amount;

    // Constructor
    public Income(String incomeName, double amount) {
        this.incomeName = incomeName;
        this.amount = amount;
    }

    // Hiển thị thông tin thu nhập
    public void displayInfo() {
        System.out.println("Income Name: " + incomeName);
        System.out.println("Amount: " + amount);
    }

    // Getter cho incomeName
    public String getIncomeName() {
        return incomeName;
    }

    // Setter cho incomeName
    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
    }

    // Getter cho amount
    public double getAmount() {
        return amount;
    }

    // Setter cho amount
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
