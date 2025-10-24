package com.fund.group09.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" }) // Tránh lỗi Lazy khi convert JSON
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount; // Dùng BigDecimal để tính tiền chính xác

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now(); // Gán mặc định thời gian hiện tại

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("transactions") // Ngăn vòng lặp JSON
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore // Không trả member trong transaction JSON để tránh vòng lặp
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionType type;

    public enum TransactionType {
        INCOME, EXPENSE
    }

    // ✅ Constructors
    public Transaction() {
    }

    public Transaction(BigDecimal amount, String description, LocalDateTime date,
            Category category, Member member, TransactionType type) {
        this.amount = amount;
        this.description = description;
        this.date = date != null ? date : LocalDateTime.now();
        this.category = category;
        this.member = member;
        this.type = type;
    }

    // ✅ Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
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
