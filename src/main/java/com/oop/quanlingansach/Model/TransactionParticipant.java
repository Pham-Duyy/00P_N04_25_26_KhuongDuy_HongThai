package com.oop.quanlingansach.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_participants")
public class TransactionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết tới Transaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    // Liên kết tới User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Số tiền đóng góp
    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    // Đã đóng tiền hay chưa
    @Column(nullable = false)
    private boolean paid = false;

    // Ngày xác nhận đã đóng tiền (có thể null)
    private LocalDateTime paidDate;

    // Constructors
    public TransactionParticipant() {}

    public TransactionParticipant(Transaction transaction, User user, BigDecimal amount) {
        this.transaction = transaction;
        this.user = user;
        this.amount = amount;
        this.paid = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    // Trạng thái đóng góp (hiển thị cho giao diện)
    @Transient
    public String getPaidStatus() {
        if (paid) {
            return "Đã đóng";
        } else {
            return "Chưa đóng";
        }
    }
}