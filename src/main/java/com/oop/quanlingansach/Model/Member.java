package com.oop.quanlingansach.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "group_members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết tới Group
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // Liên kết tới User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_date")
    private LocalDateTime joinedDate;

    @Column(length = 20)
    private String status; // "PENDING", "APPROVED", "REJECTED"

    @Column(name = "request_message", length = 500)
    private String requestMessage;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Column(name = "approved_by")
    private Long approvedBy; // ID admin duyệt

    @Column(length = 500)
    private String note;

    // Constructors
    public Member() {}

    public Member(Group group, User user, String requestMessage) {
        this.group = group;
        this.user = user;
        this.requestMessage = requestMessage;
        this.status = "PENDING";
        this.requestDate = LocalDateTime.now();
    }

    public Member(Group group, User user, String status, boolean isDirectApproval) {
        this.group = group;
        this.user = user;
        this.status = status;
        if ("APPROVED".equals(status)) {
            this.joinedDate = LocalDateTime.now();
        }
        this.requestDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getJoinedDate() { return joinedDate; }
    public void setJoinedDate(LocalDateTime joinedDate) { this.joinedDate = joinedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRequestMessage() { return requestMessage; }
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }

    public LocalDateTime getResponseDate() { return responseDate; }
    public void setResponseDate(LocalDateTime responseDate) { this.responseDate = responseDate; }

    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    // Business Methods
    public boolean isPending() {
        return "PENDING".equals(this.status);
    }

    public boolean isApproved() {
        return "APPROVED".equals(this.status);
    }

    public boolean isRejected() {
        return "REJECTED".equals(this.status);
    }

    public void approve(Long adminId, String note) {
        this.status = "APPROVED";
        this.approvedBy = adminId;
        this.note = note;
        this.responseDate = LocalDateTime.now();
        this.joinedDate = LocalDateTime.now();
    }

    public void reject(Long adminId, String note) {
        this.status = "REJECTED";
        this.approvedBy = adminId;
        this.note = note;
        this.responseDate = LocalDateTime.now();
    }

    public void resetToPending() {
        this.status = "PENDING";
        this.approvedBy = null;
        this.note = null;
        this.responseDate = null;
        this.joinedDate = null;
    }

    public boolean hasResponse() {
        return responseDate != null;
    }

    public long getDaysSinceRequest() {
        return requestDate != null ? java.time.Duration.between(requestDate, LocalDateTime.now()).toDays() : 0;
    }

    public long getDaysSinceJoined() {
        if (joinedDate == null) return 0;
        return java.time.Duration.between(joinedDate, LocalDateTime.now()).toDays();
    }

    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", group=" + (group != null ? group.getId() : null) +
                ", user=" + (user != null ? user.getId() : null) +
                ", joinedDate=" + joinedDate +
                ", status='" + status + '\'' +
                ", requestMessage='" + requestMessage + '\'' +
                ", requestDate=" + requestDate +
                ", responseDate=" + responseDate +
                ", approvedBy=" + approvedBy +
                ", note='" + note + '\'' +
                '}';
    }
}