package com.fund.group09.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transaction_group_id", columnList = "group_id"),
    @Index(name = "idx_transaction_created_by", columnList = "created_by"),
    @Index(name = "idx_transaction_date", columnList = "date"),
    @Index(name = "idx_transaction_status", columnList = "status"),
    @Index(name = "idx_transaction_type", columnList = "type")
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.01", message = "Số tiền phải lớn hơn 0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 3, max = 500, message = "Mô tả phải từ 3-500 ký tự")
    @Column(length = 500, nullable = false)
    private String description;

    @NotNull(message = "Loại giao dịch không được để trống")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TransactionType type;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @NotBlank(message = "Trạng thái không được để trống")
    @Pattern(regexp = "^(PENDING|COMPLETED|CANCELLED|FAILED)$", 
             message = "Trạng thái phải là PENDING, COMPLETED, CANCELLED hoặc FAILED")
    @Column(name = "status", nullable = false, length = 20)
    private String status = Status.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id", nullable = true)  // ĐỔI nullable = true
private Category category;

    @NotNull(message = "Nhóm không được để trống")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @NotNull(message = "Người tạo không được để trống")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id", nullable = true)  // ← THÊM FIELD NÀY
    private User payer;
    @Column(name = "approved", nullable = false)
    private Boolean approved = false; 
    // Status constants để dùng trong code
    public static final class Status {
        public static final String PENDING = "PENDING";
        public static final String COMPLETED = "COMPLETED";
        public static final String CANCELLED = "CANCELLED";
        public static final String FAILED = "FAILED";
        private Status() {}
    }

// ✅ THÊM GETTER/SETTER CHO PAYER VÀ APPROVED
    public User getPayer() { return payer; }
    public void setPayer(User payer) { this.payer = payer; }

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }


    // Enum loại giao dịch
    public enum TransactionType {
        INCOME("Thu"),
        EXPENSE("Chi");

        private final String displayName;

        TransactionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    // Constructors
    public Transaction() {
        this.date = LocalDateTime.now();
        this.createdDate = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    public Transaction(BigDecimal amount, String description, TransactionType type) {
        this();
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public Transaction(BigDecimal amount, String description, TransactionType type, Group group, User createdBy) {
        this(amount, description, type);
        this.group = group;
        this.createdBy = createdBy;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
        if (this.status == null || this.status.isEmpty()) {
            this.status = Status.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    // Business methods
    public boolean isPending() {
        return Status.PENDING.equals(this.status);
    }

    public boolean isCompleted() {
        return Status.COMPLETED.equals(this.status);
    }

    public boolean isCancelled() {
        return Status.CANCELLED.equals(this.status);
    }

    public boolean isFailed() {
        return Status.FAILED.equals(this.status);
    }

    public boolean isIncome() {
        return TransactionType.INCOME.equals(this.type);
    }

    public boolean isExpense() {
        return TransactionType.EXPENSE.equals(this.type);
    }

    public void markCompleted() {
        this.status = Status.COMPLETED;
        this.updatedDate = LocalDateTime.now();
    }

    public void markCancelled() {
        this.status = Status.CANCELLED;
        this.updatedDate = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = Status.FAILED;
        this.updatedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

    // equals và hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString cho debugging
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", groupId=" + (group != null ? group.getId() : null) +
                ", createdById=" + (createdBy != null ? createdBy.getId() : null) +
                '}';
    }

    // Display methods cho UI
    public String getFormattedAmount() {
        return String.format("%,.0f₫", amount.doubleValue());
    }

    public String getFormattedDate() {
        return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getStatusDisplayName() {
        switch (status) {
            case Status.PENDING: return "Chờ xử lý";
            case Status.COMPLETED: return "Hoàn thành";
            case Status.CANCELLED: return "Đã hủy";
            case Status.FAILED: return "Thất bại";
            default: return status;
        }
    }

    public String getTypeDisplayName() {
        return type != null ? type.getDisplayName() : "";
    }
}