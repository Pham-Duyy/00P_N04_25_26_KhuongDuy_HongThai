package com.example.servingwebcontent.model;

public class Member {
    private Long id;
    private String name;
    private String email;
    private Role role;

    // Getters and setters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public Role getRole() {
        return role;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
    
