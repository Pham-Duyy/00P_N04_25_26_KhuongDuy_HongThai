package com.oop.quanlingansach.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Report {
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private LocalDateTime createdDate;
    private Long groupId;
    private Long createdBy;

    public Report() {
    }

    public Report(Long id, String title, String description, BigDecimal amount, LocalDateTime createdDate, Long groupId, Long createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.createdDate = createdDate;
        this.groupId = groupId;
        this.createdBy = createdBy;
    }

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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
}