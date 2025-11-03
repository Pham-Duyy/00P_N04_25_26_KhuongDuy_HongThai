package com.oop.quanlingansach.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 20)
    private String type; // "INCOME" (thu), "EXPENSE" (chi)

    // Liên kết tới Group
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // Liên kết tới User (admin tạo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate; // Hạn đóng góp

    @Column(nullable = false, length = 20)
    private String status; // "ACTIVE", "COMPLETED", "CANCELLED"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    // Liên kết tới các participant (thành viên tham gia giao dịch)
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransactionParticipant> participants;

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public List<TransactionParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<TransactionParticipant> participants) {
        this.participants = participants;
    }

    // Constructors
    public Transaction() {}

    public Transaction(String title, String description, BigDecimal amount,
                       String type, Group group, User createdBy, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.group = group;
        this.createdBy = createdBy;
        this.dueDate = dueDate;
        this.createdDate = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    public Transaction(Long id, String title, String description, BigDecimal amount,
                       String type, Group group, User createdBy, LocalDateTime createdDate,
                       LocalDateTime dueDate, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.group = group;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Thêm getter/setter cho "name" để tương thích Thymeleaf
    @Transient
    public String getName() {
        return this.title;
    }

    @Transient
    public void setName(String name) {
        this.title = name;
    }

    // Business Methods
    public boolean isIncome() {
        return "INCOME".equalsIgnoreCase(this.type);
    }

    public boolean isExpense() {
        return "EXPENSE".equalsIgnoreCase(this.type);
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(this.status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(this.status);
    }

    public void complete() {
        this.status = "COMPLETED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void activate() {
        this.status = "ACTIVE";
    }

    public boolean isOverdue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate) && isActive();
    }

    public long getDaysUntilDue() {
        if (dueDate == null) return -1;
        return java.time.Duration.between(LocalDateTime.now(), dueDate).toDays();
    }

    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction tx)) return false;
        return Objects.equals(id, tx.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", group=" + (group != null ? group.getId() : null) +
                ", createdBy=" + (createdBy != null ? createdBy.getId() : null) +
                ", createdDate=" + createdDate +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }

    // Validation methods
    public boolean isValidTransaction() {
        return title != null && !title.trim().isEmpty() &&
               amount != null && amount.compareTo(BigDecimal.ZERO) > 0 &&
               type != null && (type.equalsIgnoreCase("INCOME") || type.equalsIgnoreCase("EXPENSE")) &&
               group != null && createdBy != null &&
               status != null && (status.equalsIgnoreCase("ACTIVE") ||
                                  status.equalsIgnoreCase("COMPLETED") ||
                                  status.equalsIgnoreCase("CANCELLED"));
    }

    public boolean isValidAmount() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isValidType() {
        return type != null && (type.equalsIgnoreCase("INCOME") || type.equalsIgnoreCase("EXPENSE"));
    }

    public boolean isValidStatus() {
        return status != null && (status.equalsIgnoreCase("ACTIVE") ||
                                  status.equalsIgnoreCase("COMPLETED") ||
                                  status.equalsIgnoreCase("CANCELLED"));
    }
}