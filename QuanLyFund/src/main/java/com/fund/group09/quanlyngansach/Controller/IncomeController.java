package com.fund.group09.quanlyngansach.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import  org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fund.group09.quanlyngansach.Model.Income;
import com.fund.group09.quanlyngansach.Repository.IncomeRepository;

@RestController
@RequestMapping("/incomes")
@CrossOrigin(origins = "*")
public class IncomeController {

    @Autowired
    private IncomeRepository incomeRepository;

    // ✅ Lấy tất cả income
    @GetMapping
    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    // ✅ Lấy income theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Income> getIncomeById(@PathVariable Long id) {
        return incomeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Thêm income mới
    @PostMapping
    public ResponseEntity<Income> addIncome(@RequestBody Income income) {
        Income saved = incomeRepository.save(income);
        return ResponseEntity.status(201).body(saved);
    }

    // ✅ Cập nhật income
    @PutMapping("/{id}")
    public ResponseEntity<Income> updateIncome(@PathVariable Long id, @RequestBody Income newIncome) {
        return incomeRepository.findById(id)
                .map(income -> {
                    income.setAmount(newIncome.getAmount());
                    income.setSource(newIncome.getSource());
                    income.setDate(newIncome.getDate());
                    income.setMember(newIncome.getMember());
                    income.setCategory(newIncome.getCategory());
                    Income updated = incomeRepository.save(income);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Xóa income
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        if (!incomeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        incomeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
