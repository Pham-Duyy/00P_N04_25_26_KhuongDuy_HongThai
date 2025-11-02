package com.oop.quanlingansach.Model;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Objects;

public class Group {
    private Long id;
    private String name;
    private String description;
    private Long adminId; // ID của admin tạo nhóm
    private LocalDateTime createdDate;
    private BigDecimal totalBudget;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private boolean isActive;
    
    // Constructors
    public Group() {}
    
    public Group(String name, String description, Long adminId) {
        this.name = name;
        this.description = description;
        this.adminId = adminId;
        this.createdDate = LocalDateTime.now();
        this.totalBudget = BigDecimal.ZERO;
        this.totalIncome = BigDecimal.ZERO;
        this.totalExpense = BigDecimal.ZERO;
        this.isActive = true;
    }
    
    public Group(Long id, String name, String description, Long adminId, 
                LocalDateTime createdDate, BigDecimal totalBudget, 
                BigDecimal totalIncome, BigDecimal totalExpense, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.adminId = adminId;
        this.createdDate = createdDate;
        this.totalBudget = totalBudget;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public Long getAdminId() { 
        return adminId; 
    }
    
    public void setAdminId(Long adminId) { 
        this.adminId = adminId; 
    }
    
    public LocalDateTime getCreatedDate() { 
        return createdDate; 
    }
    
    public void setCreatedDate(LocalDateTime createdDate) { 
        this.createdDate = createdDate; 
    }
    
    public BigDecimal getTotalBudget() { 
        return totalBudget; 
    }
    
    public void setTotalBudget(BigDecimal totalBudget) { 
        this.totalBudget = totalBudget; 
    }
    
    public BigDecimal getTotalIncome() { 
        return totalIncome; 
    }
    
    public void setTotalIncome(BigDecimal totalIncome) { 
        this.totalIncome = totalIncome; 
    }
    
    public BigDecimal getTotalExpense() { 
        return totalExpense; 
    }
    
    public void setTotalExpense(BigDecimal totalExpense) { 
        this.totalExpense = totalExpense; 
    }
    
    public boolean isActive() { 
        return isActive; 
    }
    
    public void setActive(boolean active) { 
        isActive = active; 
    }
    
    // Business Methods
    public BigDecimal getCurrentBalance() {
        return totalIncome.subtract(totalExpense);
    }
    
    public void addIncome(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.totalIncome = this.totalIncome.add(amount);
            this.totalBudget = this.totalBudget.add(amount);
        }
    }
    
    public void addExpense(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.totalExpense = this.totalExpense.add(amount);
        }
    }
    
    public boolean canManageGroup(User user) {
        return user != null && user.isAdmin() && user.getId().equals(this.adminId);
    }
    
    public boolean canEditGroup(User user) {
        return user != null && user.isAdmin() && user.getId().equals(this.adminId);
    }
    
    public boolean canDeleteGroup(User user) {
        return user != null && user.isAdmin() && user.getId().equals(this.adminId);
    }
    
    public boolean canViewGroup(User user) {
        return user != null && (user.isUser() || user.isAdmin());
    }
    
    public boolean canJoinGroup(User user) {
        return user != null && user.isUser() && user.getId() != null && 
               !user.getId().equals(this.adminId);
    }
    
    public void activate() {
        this.isActive = true;
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public boolean hasBalance() {
        return getCurrentBalance().compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean hasDeficit() {
        return getCurrentBalance().compareTo(BigDecimal.ZERO) < 0;
    }
    
    public double getIncomePercentage() {
        if (totalBudget.compareTo(BigDecimal.ZERO) == 0) return 0;
        return totalIncome.divide(totalBudget, 2, BigDecimal.ROUND_HALF_UP)
                         .multiply(BigDecimal.valueOf(100)).doubleValue();
    }
    
    public double getExpensePercentage() {
        if (totalBudget.compareTo(BigDecimal.ZERO) == 0) return 0;
        return totalExpense.divide(totalBudget, 2, BigDecimal.ROUND_HALF_UP)
                          .multiply(BigDecimal.valueOf(100)).doubleValue();
    }
    
    public long getDaysSinceCreation() {
        return java.time.Duration.between(createdDate, LocalDateTime.now()).toDays();
    }
    
    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && 
               Objects.equals(name, group.name) && 
               Objects.equals(adminId, group.adminId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, adminId);
    }
    
    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", adminId=" + adminId +
                ", createdDate=" + createdDate +
                ", totalBudget=" + totalBudget +
                ", totalIncome=" + totalIncome +
                ", totalExpense=" + totalExpense +
                ", currentBalance=" + getCurrentBalance() +
                ", isActive=" + isActive +
                '}';
    }
    
    // Validation methods
    public boolean isValidGroup() {
        return name != null && !name.trim().isEmpty() &&
               adminId != null &&
               totalBudget != null && totalBudget.compareTo(BigDecimal.ZERO) >= 0 &&
               totalIncome != null && totalIncome.compareTo(BigDecimal.ZERO) >= 0 &&
               totalExpense != null && totalExpense.compareTo(BigDecimal.ZERO) >= 0 &&
               createdDate != null;
    }
    
    public boolean isValidName() {
        return name != null && name.trim().length() >= 3 && name.trim().length() <= 100;
    }
    
    public boolean isValidDescription() {
        return description == null || description.length() <= 500;
    }
    
    public boolean canBeDeleted() {
        return isActive && totalBudget.compareTo(BigDecimal.ZERO) == 0;
    }
    
    public boolean hasTransactions() {
        return totalIncome.compareTo(BigDecimal.ZERO) > 0 || 
               totalExpense.compareTo(BigDecimal.ZERO) > 0;
    }
}