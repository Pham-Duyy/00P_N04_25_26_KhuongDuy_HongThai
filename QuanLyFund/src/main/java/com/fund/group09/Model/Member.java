package com.fund.group09.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // ✅ Fix: Map to correct database column name
    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "role", length = 20, nullable = false)
    private String role = "MEMBER"; // Default value

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Income> incomes = new ArrayList<>();

    // ✅ Enhanced Constructors
    public Member() {
        this.joinedAt = LocalDateTime.now();
        this.role = "MEMBER";
    }

    public Member(User user, Group group, String role) {
        this.user = user;
        this.group = group;
        this.role = role != null ? role : "MEMBER";
        this.joinedAt = LocalDateTime.now();
        if (user != null) {
            this.name = user.getName();
            this.email = user.getEmail();
        }
    }

    // ✅ Add PrePersist to ensure required fields
    @PrePersist
    public void prePersist() {
        if (this.joinedAt == null) {
            this.joinedAt = LocalDateTime.now();
        }
        if (this.role == null || this.role.trim().isEmpty()) {
            this.role = "MEMBER";
        }
        if (this.user != null) {
            if (this.name == null || this.name.trim().isEmpty()) {
                this.name = this.user.getName();
            }
            if (this.email == null || this.email.trim().isEmpty()) {
                this.email = this.user.getEmail();
            }
        }
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            if (this.name == null || this.name.trim().isEmpty()) {
                this.name = user.getName();
            }
            if (this.email == null || this.email.trim().isEmpty()) {
                this.email = user.getEmail();
            }
        }
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role != null ? role : "MEMBER";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
    }

    // Helper methods
    public void addExpense(Expense expense) {
        expenses.add(expense);
        expense.setMember(this);
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
        expense.setMember(null);
    }

    public void addIncome(Income income) {
        incomes.add(income);
        income.setMember(this);
    }

    public void removeIncome(Income income) {
        incomes.remove(income);
        income.setMember(null);
    }

    // ✅ Enhanced validation method
    public boolean isValid() {
        return user != null && 
               group != null && 
               name != null && !name.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               role != null && !role.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return id != null && id.equals(member.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Member{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", role='" + role + '\'' +
               ", joinedAt=" + joinedAt +
               ", userId=" + (user != null ? user.getId() : null) +
               ", groupId=" + (group != null ? group.getId() : null) +
               '}';
    }
}