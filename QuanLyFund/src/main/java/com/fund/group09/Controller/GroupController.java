package com.fund.group09.Controller;

import com.fund.group09.Model.Group;
import com.fund.group09.Model.Expense;
import com.fund.group09.Model.Income;
import com.fund.group09.Repository.GroupRepository;
import com.fund.group09.Repository.ExpenseRepository;
import com.fund.group09.Repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    // Lấy tất cả nhóm
    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // Lấy nhóm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo nhóm mới
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group saved = groupRepository.save(group);
        return ResponseEntity.status(201).body(saved);
    }

    // Cập nhật nhóm
    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody Group newGroup) {
        return groupRepository.findById(id)
                .map(group -> {
                    group.setName(newGroup.getName());
                    group.setDescription(newGroup.getDescription());
                    group.setMembers(newGroup.getMembers());
                    group.setFund(newGroup.getFund());
                    Group updated = groupRepository.save(group);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Xóa nhóm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        if (!groupRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        groupRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // API thống kê tổng thu - chi - quỹ của nhóm
    @GetMapping("/{id}/summary")
    public ResponseEntity<Map<String, Object>> getGroupSummary(@PathVariable Long id) {
        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Group group = groupOpt.get();

        // Lấy toàn bộ expense & income của nhóm (thông qua các member)
        List<Expense> expenses = expenseRepository.findAll();
        List<Income> incomes = incomeRepository.findAll();

        double totalExpense = expenses.stream()
                .filter(e -> e.getMember() != null && e.getMember().getGroup() != null
                        && e.getMember().getGroup().getId().equals(id))
                .mapToDouble(Expense::getAmount)
                .sum();

        double totalIncome = incomes.stream()
                .filter(i -> i.getMember() != null && i.getMember().getGroup() != null
                        && i.getMember().getGroup().getId().equals(id))
                .mapToDouble(Income::getAmount)
                .sum();

        double currentBalance = totalIncome - totalExpense;

        Map<String, Object> summary = new HashMap<>();
        summary.put("groupName", group.getName());
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("currentBalance", currentBalance);
        summary.put("membersCount", group.getMembers() != null ? group.getMembers().size() : 0);

        return ResponseEntity.ok(summary);
    }
}
