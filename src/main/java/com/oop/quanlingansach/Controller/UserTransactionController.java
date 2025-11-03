package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.Transaction;
import com.oop.quanlingansach.Model.TransactionParticipant;
import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.TransactionParticipantService;
import com.oop.quanlingansach.Service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/user/finance")
public class UserTransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionParticipantService transactionParticipantService;

    // Hiển thị danh sách giao dịch thu và chi mà user nhận được
    @GetMapping("/transactions")
    public String listUserTransactions(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        // Giao dịch thu cần đóng
        List<Transaction> incomeTransactions = transactionService.findIncomeTransactionsForUser(user.getId());
        // Giao dịch chi nhận thông báo
        List<Transaction> expenseNotifications = transactionService.findExpenseNotificationsForUser(user.getId());
        model.addAttribute("incomeTransactions", incomeTransactions);
        model.addAttribute("expenseNotifications", expenseNotifications);
        return "user/finance/transactions";
    }

    // Xác nhận đã chuyển tiền
    @PostMapping("/transactions/{transactionId}/confirm")
    public String confirmPayment(@PathVariable Long transactionId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // Lấy participant theo transactionId và userId
        TransactionParticipant participant = transactionParticipantService.findByTransactionIdAndUserId(transactionId, user.getId());
        if (participant != null) {
            participant.setPaid(true);
            participant.setPaidDate(LocalDateTime.now());
            transactionParticipantService.save(participant);
        } else {
            // Nếu chưa có participant, tạo mới (phòng trường hợp thiếu dữ liệu)
            Transaction transaction = transactionService.getTransactionById(transactionId).orElse(null);
            if (transaction != null) {
                TransactionParticipant newParticipant = new TransactionParticipant();
                newParticipant.setTransaction(transaction);
                newParticipant.setUser(user);
                newParticipant.setPaid(true);
                newParticipant.setPaidDate(LocalDateTime.now());
                transactionParticipantService.save(newParticipant);
            }
        }

        redirectAttributes.addFlashAttribute("success", "Đã xác nhận chuyển tiền!");
        return "redirect:/user/finance/transactions";
    }
}