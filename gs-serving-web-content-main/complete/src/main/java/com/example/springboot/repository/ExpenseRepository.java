package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Expense;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class ExpenseRepository {
    private List<Expense> expenses = new ArrayList<>();

    public List<Expense> findAll() {
        return expenses;
    }

    public void save(Expense expense) {
        expenses.add(expense);
    }

    public Expense findById(Long id) {
        for (Expense e : expenses) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public boolean update(Long id, Expense newExpense) {
        Expense expense = findById(id);
        if (expense != null) {
            expense.setAmount(newExpense.getAmount());
            expense.setDescription(newExpense.getDescription());
            expense.setDate(newExpense.getDate());
            expense.setMember(newExpense.getMember());
            expense.setCategory(newExpense.getCategory());
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        Expense expense = findById(id);
        if (expense != null) {
            expenses.remove(expense);
            return true;
        }
        return false;
    }
}
