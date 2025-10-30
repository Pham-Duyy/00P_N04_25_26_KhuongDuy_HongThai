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

        // ✅ Truyền biến kiểm tra role để hiển thị nút "Tạo giao dịch mới"
        if (principal != null) {
            User currentUser = userService.findByEmail(principal.getName());
            model.addAttribute("isAdmin", currentUser != null && "ADMIN".equals(currentUser.getRole()));
        } else {
            model.addAttribute("isAdmin", false);
        }

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
}
