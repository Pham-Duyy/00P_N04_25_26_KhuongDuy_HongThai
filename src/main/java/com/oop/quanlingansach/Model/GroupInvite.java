package com.oop.quanlingansach.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_invites")
public class GroupInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lời mời thuộc nhóm nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // Lời mời gửi cho user nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Trạng thái: PENDING, ACCEPTED, DECLINED
    @Column(nullable = false)
    private String status;

    // Thời gian gửi lời mời
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    public GroupInvite() {
        this.sentAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}