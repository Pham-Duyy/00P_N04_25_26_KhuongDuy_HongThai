package com.example.servingwebcontent.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.servingwebcontent.model.Income;

@RestController
@RequestMapping("/incomes")
public class IncomeController {
    // Danh sách lưu trữ các income (giả lập database)
    private List<Income> incomes = new ArrayList<>();

    // Lấy tất cả income
    @GetMapping
    public List<Income> getAllIncomes() {
        return incomes;
    }

    // Thêm mới income
    @PostMapping
    public Income addIncome(@RequestBody Income income) {
        incomes.add(income);
        return income;
    }

    // Sửa income theo id
    @PutMapping("/{id}")
    public Income updateIncome(@PathVariable Long id, @RequestBody Income newIncome) {
        Income income = findIncomeById(id);
        if (income != null) {
            income.setAmount(newIncome.getAmount());
            income.setSource(newIncome.getSource());
            income.setDate(newIncome.getDate());
            income.setMember(newIncome.getMember());
            income.setCategory(newIncome.getCategory());
            return income;
        }
        // Không tìm thấy income
        return null;
    }

    // Xóa income theo id
    @DeleteMapping("/{id}")
    public String deleteIncome(@PathVariable Long id) {
        Income income = findIncomeById(id);
        if (income != null) {
            incomes.remove(income);
            return "Xóa thành công";
        }
        return "Không tìm thấy income";
    }

    // Hàm tiện ích tìm income theo id
    private Income findIncomeById(Long id) {
        for (Income i : incomes) {
            if (i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }
}
