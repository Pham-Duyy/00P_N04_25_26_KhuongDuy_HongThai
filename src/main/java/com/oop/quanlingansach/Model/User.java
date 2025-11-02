package com.oop.quanlingansach.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users") // tên bảng trong DB, có thể đổi tuỳ ý
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ADMIN hoặc USER

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "is_active")
    private boolean isActive;

    public enum Role {
        ADMIN, USER
    }

    // Constructors
    public User() {}

    public User(String username, String email, String password, String fullName, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.createdDate = LocalDateTime.now();
        this.isActive = true;
    }

    public User(Long id, String username, String email, String password,
                String fullName, Role role, LocalDateTime createdDate, boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.createdDate = createdDate;
        this.isActive = isActive;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public void setRole(String roleStr) {
        if ("ADMIN".equalsIgnoreCase(roleStr)) {
            this.role = Role.ADMIN;
        } else {
            this.role = Role.USER;
        }
    }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    // Business Methods
    public boolean isAdmin() { return Role.ADMIN.equals(this.role); }
    public boolean isUser() { return Role.USER.equals(this.role); }
    public void activate() { this.isActive = true; }
    public void deactivate() { this.isActive = false; }

    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", createdDate=" + createdDate +
                ", isActive=" + isActive +
                '}';
    }

    // Validation methods
    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    public boolean isValidUsername() {
        return username != null && username.length() >= 3 && username.length() <= 50;
    }

    public boolean isValidPassword() {
        return password != null && password.length() >= 6;
    }

    public boolean isValidUser() {
        return isValidUsername() && isValidEmail() && isValidPassword() &&
                fullName != null && !fullName.trim().isEmpty() &&
                role != null;
    }
}