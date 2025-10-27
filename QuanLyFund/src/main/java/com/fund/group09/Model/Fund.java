package com.fund.group09.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "funds")
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double balance = 0.0;

    private String description;

    @OneToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public Fund() {
    }

    public Fund(Double balance, String description, Group group) {
        this.balance = balance;
        this.description = description;
        this.group = group;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    // Thêm phương thức tiện ích để lấy số dư (giúp code controller rõ ràng hơn)
    public double getAmount() {
        return balance != null ? balance : 0.0;
    }
}