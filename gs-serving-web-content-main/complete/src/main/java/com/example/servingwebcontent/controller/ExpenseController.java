package com.example.servingwebcontent.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.servingwebcontent.model.Expense;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    // Danh sách lưu trữ các expense (giả lập database)
    private List<Expense> expenses = new ArrayList<>();

    // Lấy tất cả expense
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenses;
    }

    // Thêm mới expense
    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        expenses.add(expense);
        return expense;
    }

    // Sửa expense theo id
    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense newExpense) {
        Expense expense = findExpenseById(id);
        if (expense != null) {
            expense.setAmount(newExpense.getAmount());
            expense.setDescription(newExpense.getDescription());
            expense.setDate(newExpense.getDate());
            expense.setMember(newExpense.getMember());
            expense.setCategory(newExpense.getCategory());
            return expense;
        }
        // Không tìm thấy expense
        return null;
    }

    // Xóa expense theo id
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {
        Expense expense = findExpenseById(id);
        if (expense != null) {
            expenses.remove(expense);
            return "Xóa thành công";
        }
        return "Không tìm thấy expense";
    }

    // Hàm tiện ích tìm expense theo id
    private Expense findExpenseById(Long id) {
        for (Expense e : expenses) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }
}
