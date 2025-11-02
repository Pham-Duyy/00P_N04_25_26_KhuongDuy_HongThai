package com.oop.quanlingansach.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "`groups`") // Sử dụng backtick để tránh lỗi reserved keyword MySQL
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "admin_id", nullable = false)
    private Long adminId; // ID của admin tạo nhóm

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "total_budget", nullable = false)
    private BigDecimal totalBudget = BigDecimal.ZERO;

    @Column(name = "total_income", nullable = false)
    private BigDecimal totalIncome = BigDecimal.ZERO;

    @Column(name = "total_expense", nullable = false)
    private BigDecimal totalExpense = BigDecimal.ZERO;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(length = 50)
    private String type; // Loại nhóm: FAMILY, FRIENDS, WORK, TRAVEL, OTHER

    // Thêm các trường quỹ
    @Column(name = "fund_amount")
    private BigDecimal fundAmount = BigDecimal.ZERO;

    @Column(name = "target_amount")
    private BigDecimal targetAmount = BigDecimal.ZERO;

    // Danh sách thành viên nhóm
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members = new ArrayList<>();

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
        this.fundAmount = BigDecimal.ZERO;
        this.targetAmount = BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public BigDecimal getTotalBudget() { return totalBudget; }
    public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }

    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }

    public BigDecimal getTotalExpense() { return totalExpense; }
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getFundAmount() { return fundAmount; }
    public void setFundAmount(BigDecimal fundAmount) { this.fundAmount = fundAmount; }

    public BigDecimal getTargetAmount() { return targetAmount; }
    public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }

    public List<User> getMembers() { return members; }
    public void setMembers(List<User> members) { this.members = members; }

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

    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group group)) return false;
        return Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adminId=" + adminId +
                ", createdDate=" + createdDate +
                ", totalBudget=" + totalBudget +
                ", totalIncome=" + totalIncome +
                ", totalExpense=" + totalExpense +
                ", isActive=" + isActive +
                ", fundAmount=" + fundAmount +
                ", targetAmount=" + targetAmount +
                '}';
    }
}