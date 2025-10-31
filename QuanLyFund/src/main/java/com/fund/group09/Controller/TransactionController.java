package com.fund.group09.Controller;

import com.fund.group09.Model.Transaction;
import com.fund.group09.Model.User;
import com.fund.group09.Service.TransactionService;
import com.fund.group09.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin/finance")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    // ✅ Hiển thị danh sách giao dịch
    @GetMapping
    public String showTransactionPage(Model model, Principal principal) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        model.addAttribute("transaction", new Transaction());
        return "admin/finance/transaction";
    }
    // ✅ Hiển thị form tạo giao dịch
    @GetMapping("/transactions/new")
    public String showCreateTransactionForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("users", userService.findAll()); // Lấy danh sách người dùng
        return "admin/finance/transaction-form";
    }
    // ✅ Xử lý tạo giao dịch
    @PostMapping("/transactions")
    public String createTransaction(@Valid @ModelAttribute("transaction") Transaction transaction,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll()); // Lấy danh sách người dùng
            return "admin/finance/transaction-form";
        }
        transactionService.createTransaction(transaction);
        return "redirect:/admin/finance";
    }
    // ✅ Xử lý tạo giao dịch và gửi thông báo
    @PostMapping("/transactions/create")
public String createTransaction(@Valid @ModelAttribute("transaction") Transaction transaction,
                                BindingResult result,
                                Model model,
                                Principal principal) {
    if (principal == null) return "redirect:/login";

    User admin = userService.findByEmail(principal.getName());
    if (admin == null || !"ADMIN".equals(admin.getRole())) {
        return "redirect:/access-denied";
    }

    if (result.hasErrors()) {
        model.addAttribute("adminId", admin.getId());
        return "admin/finance/transaction-form";
    }

    transaction.setCreatedBy(admin);
    transactionService.createTransaction(transaction);

    // ✅ Gửi thông báo đến user
    User recipient = transaction.getCreatedBy(); // hoặc transaction.getCreatedBy()
    String message = String.format("""
        🧾 Yêu cầu nộp tiền:
        Bạn vừa được yêu cầu nộp tiền cho giao dịch: %s
        Số tiền: %,.0f₫
        Vui lòng chuyển khoản đến:
        STK: 9966504911
        Ngân hàng: Techcombank
        Chủ tài khoản: PHAM KHUONG DUY
        """, transaction.getDescription(), transaction.getAmount());

    // ✅ Quay về dashboard
    return "redirect:/admin/finance";
}

}
