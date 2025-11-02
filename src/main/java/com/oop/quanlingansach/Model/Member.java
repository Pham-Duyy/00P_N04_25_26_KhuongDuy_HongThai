package com.oop.quanlingansach.Model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Member {
    private Long id;
    private Long groupId;
    private Long userId;
    private LocalDateTime joinedDate;
    private String status; // "PENDING", "APPROVED", "REJECTED"
    private String requestMessage; // Lời nhắn khi gửi yêu cầu tham gia
    private LocalDateTime requestDate; // Ngày gửi yêu cầu
    private LocalDateTime responseDate; // Ngày admin phản hồi
    private Long approvedBy; // ID admin duyệt
    private String note; // Ghi chú của admin
    
    // Constructors
    public Member() {}
    
    // Constructor cho yêu cầu tham gia nhóm (với message)
    public Member(Long groupId, Long userId, String requestMessage) {
        this.groupId = groupId;
        this.userId = userId;
        this.requestMessage = requestMessage;
        this.status = "PENDING";
        this.requestDate = LocalDateTime.now();
    }
    
    // Constructor cho member đã được duyệt trực tiếp (không có message)
    public Member(Long groupId, Long userId, String status, boolean isDirectApproval) {
        this.groupId = groupId;
        this.userId = userId;
        this.status = status;
        if ("APPROVED".equals(status)) {
            this.joinedDate = LocalDateTime.now();
        }
        this.requestDate = LocalDateTime.now();
    }
    
    // Constructor đầy đủ
    public Member(Long id, Long groupId, Long userId, LocalDateTime joinedDate, 
                 String status, String requestMessage, LocalDateTime requestDate,
                 LocalDateTime responseDate, Long approvedBy, String note) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.joinedDate = joinedDate;
        this.status = status;
        this.requestMessage = requestMessage;
        this.requestDate = requestDate;
        this.responseDate = responseDate;
        this.approvedBy = approvedBy;
        this.note = note;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public Long getGroupId() { 
        return groupId; 
    }
    
    public void setGroupId(Long groupId) { 
        this.groupId = groupId; 
    }
    
    public Long getUserId() { 
        return userId; 
    }
    
    public void setUserId(Long userId) { 
        this.userId = userId; 
    }
    
    public LocalDateTime getJoinedDate() { 
        return joinedDate; 
    }
    
    public void setJoinedDate(LocalDateTime joinedDate) { 
        this.joinedDate = joinedDate; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public String getRequestMessage() { 
        return requestMessage; 
    }
    
    public void setRequestMessage(String requestMessage) { 
        this.requestMessage = requestMessage; 
    }
    
    public LocalDateTime getRequestDate() { 
        return requestDate; 
    }
    
    public void setRequestDate(LocalDateTime requestDate) { 
        this.requestDate = requestDate; 
    }
    
    public LocalDateTime getResponseDate() { 
        return responseDate; 
    }
    
    public void setResponseDate(LocalDateTime responseDate) { 
        this.responseDate = responseDate; 
    }
    
    public Long getApprovedBy() { 
        return approvedBy; 
    }
    
    public void setApprovedBy(Long approvedBy) { 
        this.approvedBy = approvedBy; 
    }
    
    public String getNote() { 
        return note; 
    }
    
    public void setNote(String note) { 
        this.note = note; 
    }
    
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
        return java.time.Duration.between(requestDate, LocalDateTime.now()).toDays();
    }
    
    public long getDaysSinceJoined() {
        if (joinedDate == null) return 0;
        return java.time.Duration.between(joinedDate, LocalDateTime.now()).toDays();
    }
    
    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && 
               Objects.equals(groupId, member.groupId) && 
               Objects.equals(userId, member.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, userId);
    }
    
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", joinedDate=" + joinedDate +
                ", status='" + status + '\'' +
                ", requestMessage='" + requestMessage + '\'' +
                ", requestDate=" + requestDate +
                ", responseDate=" + responseDate +
                ", approvedBy=" + approvedBy +
                ", note='" + note + '\'' +
                '}';
    }
    
    // Validation methods
    public boolean isValidMember() {
        return groupId != null && userId != null && 
               status != null && (status.equals("PENDING") || 
                                status.equals("APPROVED") || 
                                status.equals("REJECTED")) &&
               requestDate != null;
    }
    
    public boolean isValidStatus() {
        return status != null && (status.equals("PENDING") || 
                                status.equals("APPROVED") || 
                                status.equals("REJECTED"));
    }
    
    public boolean canBeApproved() {
        return isPending() && groupId != null && userId != null;
    }
    
    public boolean canBeRejected() {
        return isPending() && groupId != null && userId != null;
    }
}