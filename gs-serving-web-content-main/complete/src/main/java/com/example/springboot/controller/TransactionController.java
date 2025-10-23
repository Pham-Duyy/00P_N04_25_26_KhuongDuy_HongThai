package com.example.servingwebcontent.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.servingwebcontent.model.Transaction;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    // Danh sách lưu trữ các transaction (giả lập database)
    private List<Transaction> transactions = new ArrayList<>();

    // Lấy tất cả transaction
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    // Thêm mới transaction
    @PostMapping
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        transactions.add(transaction);
        return transaction;
    }

    // Sửa transaction theo id
    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction newTransaction) {
        Transaction transaction = findTransactionById(id);
        if (transaction != null) {
            transaction.setAmount(newTransaction.getAmount());
            transaction.setDescription(newTransaction.getDescription());
            transaction.setDate(newTransaction.getDate());
            transaction.setCategory(newTransaction.getCategory());
            transaction.setMember(newTransaction.getMember());
            transaction.setType(newTransaction.getType());
            return transaction;
        }
        // Không tìm thấy transaction
        return null;
    }

    // Xóa transaction theo id
    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        Transaction transaction = findTransactionById(id);
        if (transaction != null) {
            transactions.remove(transaction);
            return "Xóa thành công";
        }
        return "Không tìm thấy transaction";
    }

    // Hàm tiện ích tìm transaction theo id
    private Transaction findTransactionById(Long id) {
        for (Transaction t : transactions) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }
}
