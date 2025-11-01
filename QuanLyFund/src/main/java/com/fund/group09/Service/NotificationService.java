package com.fund.group09.Service;

import com.fund.group09.Model.Group;
import com.fund.group09.Model.Member;
import com.fund.group09.Model.Notification;
import com.fund.group09.Model.Transaction;
import com.fund.group09.Model.User;
import com.fund.group09.Repository.NotificationRepository;
import com.fund.group09.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    /**
     * Gửi thông báo cơ bản (method gốc - giữ nguyên)
     */
    public void sendNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    /**
     * Gửi thông báo yêu cầu đóng tiền cho giao dịch thu
     */
    public void sendIncomeRequestNotification(Transaction transaction, Group group) {
        try {
            System.out.println("📧 =================== SENDING INCOME REQUEST NOTIFICATION ===================");
            System.out.println("📧 Transaction ID: " + transaction.getId());
            System.out.println("📧 Group: " + group.getName());
            System.out.println("📧 Amount: " + transaction.getAmount() + " VNĐ");
            
            // Lấy danh sách thành viên trong nhóm
            List<Member> members = group.getMembers();
            if (members == null || members.isEmpty()) {
                System.out.println("📧 ⚠️ No members found in group: " + group.getName());
                return;
            }
            
            System.out.println("📧 Sending to " + members.size() + " members:");
            
            // Tạo nội dung thông báo yêu cầu đóng tiền
            String message = String.format(
                "🔔 YÊU CẦU ĐÓNG TIỀN NHÓM\n\n" +
                "📋 Nhóm: %s\n" +
                "💰 Số tiền: %,.0f VNĐ\n" +
                "📝 Mô tả: %s\n" +
                "📅 Ngày tạo: %s\n" +
                "👤 Người tạo: %s\n\n" +
                "💳 Thông tin chuyển khoản:\n" +
                "• STK: 9966504911\n" +
                "• Chủ TK: PHAM KHUONG DUY\n" +
                "• Ngân hàng: Techcombank\n" +
                "• Nội dung: [%s]-[Tên của bạn]\n\n" +
                "📱 Vui lòng chuyển khoản và thông báo lại cho admin.",
                group.getName(),
                transaction.getAmount().doubleValue(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getCreatedBy() != null ? transaction.getCreatedBy().getName() : "Admin",
                group.getName()
            );
            
            // Gửi thông báo đến từng thành viên
            int successCount = 0;
            for (Member member : members) {
                try {
                    User user = findUserFromMember(member);
                    if (user != null) {
                        sendNotification(user, message);
                        System.out.println("📧 ✅ Sent to: " + member.getName() + " (" + member.getEmail() + ")");
                        successCount++;
                    } else {
                        System.out.println("📧 ❌ User not found for member: " + member.getName());
                    }
                } catch (Exception memberEx) {
                    System.out.println("📧 ❌ Failed to send to: " + member.getName() + " - " + memberEx.getMessage());
                }
            }
            
            System.out.println("📧 ✅ Income request sent to " + successCount + "/" + members.size() + " members");
            System.out.println("📧 =================== INCOME NOTIFICATION COMPLETED ===================");
            
        } catch (Exception e) {
            System.out.println("📧 ❌ Error sending income request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo chi tiền cho giao dịch expense
     */
    public void sendExpenseNotification(Transaction transaction, Group group) {
        try {
            System.out.println("📧 =================== SENDING EXPENSE NOTIFICATION ===================");
            System.out.println("📧 Transaction ID: " + transaction.getId());
            System.out.println("📧 Group: " + group.getName());
            System.out.println("📧 Amount: " + transaction.getAmount() + " VNĐ");
            
            // Lấy danh sách thành viên trong nhóm
            List<Member> members = group.getMembers();
            if (members == null || members.isEmpty()) {
                System.out.println("📧 ⚠️ No members found in group: " + group.getName());
                return;
            }
            
            System.out.println("📧 Sending to " + members.size() + " members:");
            
            // Tạo nội dung thông báo chi tiền
            String message = String.format(
                "💳 THÔNG BÁO CHI TIỀN NHÓM\n\n" +
                "📋 Nhóm: %s\n" +
                "💰 Số tiền: %,.0f VNĐ\n" +
                "📝 Lý do: %s\n" +
                "📅 Ngày chi: %s\n" +
                "👤 Người thực hiện: %s\n\n" +
                "📊 Số tiền đã được trừ từ quỹ chung của nhóm.\n" +
                "💡 Bạn có thể xem chi tiết trong hệ thống.",
                group.getName(),
                transaction.getAmount().doubleValue(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getCreatedBy() != null ? transaction.getCreatedBy().getName() : "Admin"
            );
            
            // Gửi thông báo đến từng thành viên
            int successCount = 0;
            for (Member member : members) {
                try {
                    User user = findUserFromMember(member);
                    if (user != null) {
                        sendNotification(user, message);
                        System.out.println("📧 ✅ Sent to: " + member.getName() + " (" + member.getEmail() + ")");
                        successCount++;
                    } else {
                        System.out.println("📧 ❌ User not found for member: " + member.getName());
                    }
                } catch (Exception memberEx) {
                    System.out.println("📧 ❌ Failed to send to: " + member.getName() + " - " + memberEx.getMessage());
                }
            }
            
            System.out.println("📧 ✅ Expense notification sent to " + successCount + "/" + members.size() + " members");
            System.out.println("📧 =================== EXPENSE NOTIFICATION COMPLETED ===================");
            
        } catch (Exception e) {
            System.out.println("📧 ❌ Error sending expense notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tìm User từ Member
     */
   // ...existing code...
private User findUserFromMember(Member member) {
    try {
        // Nếu Member có User
        if (member.getUser() != null) {
            return member.getUser();
        }
        // Nếu không có, thử tìm theo email
        if (member.getEmail() != null && !member.getEmail().isEmpty()) {
            return userService.findByEmail(member.getEmail());
        }
        System.out.println("📧 ⚠️ Cannot find user for member: " + member.getName());
        return null;
    } catch (Exception e) {
        System.out.println("📧 ❌ Error finding user for member " + member.getName() + ": " + e.getMessage());
        return null;
    }
}
}