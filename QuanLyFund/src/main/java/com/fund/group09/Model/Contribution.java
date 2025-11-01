// filepath: d:\test\00P_N04_25_26_KhuongDuy_HongThai\QuanLyFund\src\main\java\com\fund\group09\Model\Contribution.java
package com.fund.group09.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "contributions")
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(nullable = false)
    private BigDecimal amountRequired; // Số tiền cần đóng (chia đều)

    @Column(nullable = false)
    private BigDecimal amountContributed = BigDecimal.ZERO; // Số tiền đã đóng

    @Column(nullable = false)
    private String status = "PENDING"; // "PENDING" hoặc "COMPLETED"

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
    public BigDecimal getAmountRequired() { return amountRequired; }
    public void setAmountRequired(BigDecimal amountRequired) { this.amountRequired = amountRequired; }
    public BigDecimal getAmountContributed() { return amountContributed; }
    public void setAmountContributed(BigDecimal amountContributed) { this.amountContributed = amountContributed; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}