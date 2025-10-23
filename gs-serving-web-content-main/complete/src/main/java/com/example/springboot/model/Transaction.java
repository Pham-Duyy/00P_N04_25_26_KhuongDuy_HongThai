package com.example.servingwebcontent.model;

import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private double amount;
    private String description;
    private LocalDateTime date;
    private Category category;
    private Member member;
    private TransactionType type; // INCOME or EXPENSE

    public enum TransactionType {
        INCOME, EXPENSE
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {            
        this.description = description;
    }   
    public LocalDateTime getDate() {
        return date;
    }           
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }   
    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;   
    }
    public TransactionType getType() {
        return type;                
    }       
    public void setType(TransactionType type) {
        this.type = type;
    }   

}
