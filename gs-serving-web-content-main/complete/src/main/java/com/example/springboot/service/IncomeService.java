package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Income;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class IncomeService {
    private List<Income> incomes = new ArrayList<>();

    public List<Income> getAllIncomes() {
        return incomes;
    }

    public Income addIncome(Income income) {
        incomes.add(income);
        return income;
    }

    public Income updateIncome(Long id, Income newIncome) {
        for (Income income : incomes) {
            if (income.getId().equals(id)) {
                income.setAmount(newIncome.getAmount());
                income.setSource(newIncome.getSource());
                income.setDate(newIncome.getDate());
                income.setMember(newIncome.getMember());
                income.setCategory(newIncome.getCategory());
                return income;
            }
        }
        return null;
    }

    public boolean deleteIncome(Long id) {
        return incomes.removeIf(i -> i.getId().equals(id));
    }

    public Income findIncomeById(Long id) {
        for (Income i : incomes) {
            if (i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }
}
