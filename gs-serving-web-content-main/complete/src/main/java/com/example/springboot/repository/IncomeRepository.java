package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Income;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class IncomeRepository {
    private List<Income> incomes = new ArrayList<>();

    public List<Income> findAll() {
        return incomes;
    }

    public void save(Income income) {
        incomes.add(income);
    }

    public Income findById(Long id) {
        for (Income i : incomes) {
            if (i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }

    public boolean update(Long id, Income newIncome) {
        Income income = findById(id);
        if (income != null) {
            income.setAmount(newIncome.getAmount());
            income.setSource(newIncome.getSource());
            income.setDate(newIncome.getDate());
            income.setMember(newIncome.getMember());
            income.setCategory(newIncome.getCategory());
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        Income income = findById(id);
        if (income != null) {
            incomes.remove(income);
            return true;
        }
        return false;
    }
}
