package com.oop.quanlingansach.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private String type; // "INCOME" (thu), "EXPENSE" (chi)
    private Long groupId;
    private Long createdBy; // Admin tạo
    private LocalDateTime createdDate;
    private LocalDateTime dueDate; // Hạn đóng góp
    private String status; // "ACTIVE", "COMPLETED", "CANCELLED"
    
    // Constructors
    public Transaction() {}
    
    public Transaction(String title, String description, BigDecimal amount, 
                      String type, Long groupId, Long createdBy, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.groupId = groupId;
        this.createdBy = createdBy;
        this.dueDate = dueDate;
        this.createdDate = LocalDateTime.now();
        this.status = "ACTIVE";
    }
    
    public Transaction(Long id, String title, String description, BigDecimal amount, 
                      String type, Long groupId, Long createdBy, LocalDateTime createdDate,
                      LocalDateTime dueDate, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.groupId = groupId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getTitle() { 
        return title; 
    }
    
    public void setTitle(String title) { 
        this.title = title; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public BigDecimal getAmount() { 
        return amount; 
    }
    
    public void setAmount(BigDecimal amount) { 
        this.amount = amount; 
    }
    
    public String getType() { 
        return type; 
    }
    
    public void setType(String type) { 
        this.type = type; 
    }
    
    public Long getGroupId() { 
        return groupId; 
    }
    
    public void setGroupId(Long groupId) { 
        this.groupId = groupId; 
    }
    
    public Long getCreatedBy() { 
        return createdBy; 
    }
    
    public void setCreatedBy(Long createdBy) { 
        this.createdBy = createdBy; 
    }
    
    public LocalDateTime getCreatedDate() { 
        return createdDate; 
    }
    
    public void setCreatedDate(LocalDateTime createdDate) { 
        this.createdDate = createdDate; 
    }
    
    public LocalDateTime getDueDate() { 
        return dueDate; 
    }
    
    public void setDueDate(LocalDateTime dueDate) { 
        this.dueDate = dueDate; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    // Business Methods
    public boolean isIncome() {
        return "INCOME".equals(this.type);
    }
    
    public boolean isExpense() {
        return "EXPENSE".equals(this.type);
    }
    
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }
    
    public boolean isCompleted() {
        return "COMPLETED".equals(this.status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equals(this.status);
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
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(title, that.title) && 
               Objects.equals(groupId, that.groupId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, title, groupId);
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", groupId=" + groupId +
                ", createdBy=" + createdBy +
                ", createdDate=" + createdDate +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }
    
    // Validation methods
    public boolean isValidTransaction() {
        return title != null && !title.trim().isEmpty() &&
               amount != null && amount.compareTo(BigDecimal.ZERO) > 0 &&
               type != null && (type.equals("INCOME") || type.equals("EXPENSE")) &&
               groupId != null && createdBy != null &&
               status != null && (status.equals("ACTIVE") || 
                                status.equals("COMPLETED") || 
                                status.equals("CANCELLED"));
    }
    
    public boolean isValidAmount() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean isValidType() {
        return type != null && (type.equals("INCOME") || type.equals("EXPENSE"));
    }
    
    public boolean isValidStatus() {
        return status != null && (status.equals("ACTIVE") || 
                                status.equals("COMPLETED") || 
                                status.equals("CANCELLED"));
    }
}