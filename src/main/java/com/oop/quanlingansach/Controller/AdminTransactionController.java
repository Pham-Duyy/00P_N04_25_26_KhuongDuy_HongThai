package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.Group;
import com.oop.quanlingansach.Model.Transaction;
import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Service.TransactionService;
import com.oop.quanlingansach.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/finance/transactions")
public class AdminTransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    // Hiển thị danh sách giao dịch tất cả nhóm
    @GetMapping
    public String listTransactions(Model model) {
        List<Transaction> transactions = transactionService.findAll();
        List<Group> groups = groupService.findAll();
        model.addAttribute("transactions", transactions);
        model.addAttribute("groups", groups);
        // Có thể thêm thống kê tổng thu/chi/số dư nếu cần
        return "admin/finance/transactions";
    }

    // Xử lý tạo giao dịch mới (từ modal hoặc form)
    @PostMapping("/create")
public String createTransaction(
        @RequestParam Long groupId,
        @RequestParam String type,
        @RequestParam BigDecimal amount,
        @RequestParam String title,
        @RequestParam(required = false) String description,
        @RequestParam(required = false, name = "targetUserId") List<String> targetUserIds,
        @RequestParam(required = false) String dueDate,
        HttpSession session,
        RedirectAttributes redirectAttributes) {

    User creator = (User) session.getAttribute("user");
    if (creator == null) {
        redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập lại!");
        return "redirect:/login";
    }
    Group group = groupService.findById(groupId);

    Transaction tx = new Transaction();
    tx.setGroup(group);
    tx.setCreatedBy(creator);
    tx.setType(type);
    tx.setAmount(amount);
    tx.setTitle(title);
    tx.setDescription(description);
    tx.setCreatedDate(LocalDateTime.now());
    tx.setStatus("ACTIVE");

    if (dueDate != null && !dueDate.isEmpty()) {
        tx.setDueDate(LocalDateTime.parse(dueDate));
    }

    // Giao dịch thu: chỉ lưu 1 transaction, gửi thông báo cho các thành viên đã chọn
    if ("INCOME".equalsIgnoreCase(type)) {
        transactionService.save(tx);

        List<User> notifyUsers;
        if (targetUserIds != null && targetUserIds.contains("ALL")) {
            notifyUsers = group.getMembers();
        } else if (targetUserIds != null) {
            notifyUsers = targetUserIds.stream()
                .filter(id -> !"ALL".equals(id))
                .map(id -> {
                    try {
                        return userService.findById(Long.parseLong(id)).orElse(null);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(u -> u != null)
                .toList();
        } else {
            notifyUsers = List.of();
        }

        for (User member : notifyUsers) {
            sendPaymentRequest(member, tx);
        }

        redirectAttributes.addFlashAttribute("success", "Tạo giao dịch thành công! Đã gửi yêu cầu đóng tiền cho thành viên.");
        return "redirect:/admin/finance/transactions";
    }

    // Giao dịch chi: gửi thông báo cho tất cả thành viên nhóm
    if ("EXPENSE".equalsIgnoreCase(type)) {
        transactionService.save(tx);
        List<User> members = group.getMembers();
        for (User member : members) {
            sendExpenseNotification(member, tx);
        }
        redirectAttributes.addFlashAttribute("success", "Tạo giao dịch chi thành công! Đã gửi thông báo cho thành viên.");
        return "redirect:/admin/finance/transactions";
    }

    transactionService.save(tx);
    redirectAttributes.addFlashAttribute("success", "Tạo giao dịch thành công!");
    return "redirect:/admin/finance/transactions";
}

    // Xem chi tiết một giao dịch
    @GetMapping("/{transactionId}/detail")
    public String viewTransaction(@PathVariable Long transactionId, Model model) {
        Transaction tx = transactionService.getTransactionById(transactionId).orElse(null);
        model.addAttribute("transaction", tx);
        return "admin/finance/transaction-detail";
    }

    // Xóa giao dịch
    @PostMapping("/{transactionId}/delete")
    public String deleteTransaction(@PathVariable Long transactionId, RedirectAttributes redirectAttributes) {
        transactionService.deleteById(transactionId);
        redirectAttributes.addFlashAttribute("success", "Đã xóa giao dịch!");
        return "redirect:/admin/finance/transactions";
    }

    // API lấy thành viên nhóm (dùng cho ajax load thành viên khi chọn nhóm)
    @GetMapping("/group/{groupId}/members")
    @ResponseBody
    public List<User> getGroupMembers(@PathVariable Long groupId) {
        Group group = groupService.findById(groupId);
        return group != null ? group.getMembers() : List.of();
    }

    // Gửi yêu cầu đóng tiền cho user (có thể là notification/email)
    private void sendPaymentRequest(User user, Transaction tx) {
        // TODO: Thay bằng logic gửi thực tế (notification/email)
        // Ví dụ gửi email:
        String subject = "Yêu cầu đóng tiền cho giao dịch: " + tx.getTitle();
        String content = "<b>Vui lòng chuyển khoản theo thông tin sau:</b><br>"
                + "🏛️ Ngân hàng: <b>Techcombank</b><br>"
                + "📧 STK: <b>9966504911</b><br>"
                + "👤 Chủ TK: <b>PHAM KHUONG DUY</b><br>"
                + "<img src='https://your-domain/img/anh%20QR.jpg' alt='QR Techcombank' style='width:180px;height:180px;border-radius:8px;border:1px solid #eee;'><br>"
                + "Nội dung: " + tx.getTitle() + "<br>"
                + "Số tiền: " + tx.getAmount() + " VNĐ";
        // userService.sendEmail(user.getEmail(), subject, content); // Nếu có hàm này
    }

    // Gửi thông báo chi cho user (notification/email)
    private void sendExpenseNotification(User user, Transaction tx) {
        // TODO: Thay bằng logic gửi thực tế (notification/email)
        String subject = "Thông báo chi: " + tx.getTitle();
        String content = "Bạn vừa nhận được thông báo chi: " + tx.getTitle()
                + "<br>Số tiền: " + tx.getAmount() + " VNĐ";
        // userService.sendEmail(user.getEmail(), subject, content); // Nếu có hàm này
    }
}