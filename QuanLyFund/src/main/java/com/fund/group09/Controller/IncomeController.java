package com.fund.group09.Controller;

import com.fund.group09.Model.Income;
import com.fund.group09.Repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
@CrossOrigin(origins = "*") // Cho phép frontend gọi API
public class IncomeController {

    @Autowired
    private IncomeRepository incomeRepository;

    // Lấy tất cả income
    @GetMapping
    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    // Lấy income theo id
    @GetMapping("/{id}")
    public ResponseEntity<Income> getIncomeById(@PathVariable Long id) {
        return incomeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Thêm mới income
    @PostMapping
    public ResponseEntity<Income> addIncome(@RequestBody Income income) {
        Income saved = incomeRepository.save(income);
        return ResponseEntity.status(201).body(saved);
    }

    // Cập nhật income
    @PutMapping("/{id}")
    public ResponseEntity<Income> updateIncome(@PathVariable Long id, @RequestBody Income newIncome) {
        return incomeRepository.findById(id)
                .map(income -> {
                    income.setAmount(newIncome.getAmount());
                    income.setDescription(newIncome.getDescription());
                    income.setDate(newIncome.getDate());
                    income.setMember(newIncome.getMember());
                    Income updated = incomeRepository.save(income);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Xóa income
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        if (!incomeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        incomeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
