package com.fund.group09.Controller;

import com.fund.group09.Model.Transaction;
import com.fund.group09.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Hiển thị giao diện danh sách giao dịch
    @GetMapping("/admin/finance")
    public String showTransactionPage(Model model) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "admin/finance/transaction"; // Trả về đúng file transaction.html
    }

    // Hiển thị form tạo giao dịch mới
    @GetMapping("/transactions/new")
    public String showCreateForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "admin/finance/transaction-form";
    }

}
