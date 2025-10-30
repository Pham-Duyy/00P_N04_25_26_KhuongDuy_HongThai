package com.fund.group09.Service;

import com.fund.group09.Model.Expense;
import com.fund.group09.Repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository repo;

    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public List<Expense> getAll() {
        return repo.findAll();
    }

    public Expense getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Expense save(Expense expense) {
        return repo.save(expense);
    }

    public Expense update(Long id, Expense newExpense) {
        return repo.findById(id).map(expense -> {
            expense.setAmount(newExpense.getAmount());
            expense.setDescription(newExpense.getDescription());
            expense.setDate(newExpense.getDate());
            expense.setMember(newExpense.getMember());
            expense.setCategory(newExpense.getCategory());
            return repo.save(expense);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (!repo.existsById(id))
            return false;
        repo.deleteById(id);
        return true;
    }
}
