package com.example.servingwebcontent.model;

import java.time.LocalDateTime;

public class Income {
    private Long id;
    private double amount;
    private String source;
    private LocalDateTime date;
    private Member member;
    private Category category;

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
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;   
    }
    public Category getCategory() {
        return category;
    }   
    public void setCategory(Category category) {
        this.category = category;
    }             
}   
    
