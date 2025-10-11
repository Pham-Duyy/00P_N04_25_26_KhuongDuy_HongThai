package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Expense;
import com.example.servingwebcontent.repository.ExpenseRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense addExpense(Expense expense) {
        expenseRepository.save(expense);
        return expense;
    }

    public Expense updateExpense(Long id, Expense newExpense) {
        boolean updated = expenseRepository.update(id, newExpense);
        if (updated) {
            return expenseRepository.findById(id);
        }
        return null;
    }

    public boolean deleteExpense(Long id) {
        return expenseRepository.delete(id);
    }

    public Expense findExpenseById(Long id) {
        return expenseRepository.findById(id);
    }
}
