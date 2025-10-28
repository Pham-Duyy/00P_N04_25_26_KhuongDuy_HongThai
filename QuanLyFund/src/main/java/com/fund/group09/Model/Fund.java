package com.fund.group09.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "funds")
public class Fund implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Balance cannot be null")
    @PositiveOrZero(message = "Balance must be positive or zero")
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(length = 500)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    // Default constructor
    public Fund() {
    }

    // Constructor with parameters
    public Fund(BigDecimal balance, String description, Group group) {
        this.setBalance(balance);
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

    public BigDecimal getBalance() {
        return balance != null ? balance : BigDecimal.ZERO;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance != null ? balance : BigDecimal.ZERO;
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
}