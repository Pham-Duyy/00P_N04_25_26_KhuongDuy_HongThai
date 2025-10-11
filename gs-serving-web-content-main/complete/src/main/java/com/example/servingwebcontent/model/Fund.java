package com.example.servingwebcontent.model;

public class Fund {
    private Long id;
    private double balance;
    private Group group;

    // Getters and setters
    public Long getId() {
        return id;
    }
    public double getBalance() {
        return balance;
    }
    public Group getGroup() {
        return group;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setGroup(Group group) {
        this.group = group;
    }   
}
