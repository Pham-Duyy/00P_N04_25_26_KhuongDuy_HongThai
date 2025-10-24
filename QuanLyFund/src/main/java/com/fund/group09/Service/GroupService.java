package com.fund.group09.Service;

import com.fund.group09.Model.Group;
import com.fund.group09.Model.Expense;
import com.fund.group09.Model.Income;
import com.fund.group09.Repository.GroupRepository;
import com.fund.group09.Repository.ExpenseRepository;
import com.fund.group09.Repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public GroupService(GroupRepository groupRepository,
                        ExpenseRepository expenseRepository,
                        IncomeRepository incomeRepository) {
        this.groupRepository = groupRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    //  Lấy tất cả nhóm
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    //  Tìm nhóm theo ID
    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    //  Tạo hoặc lưu nhóm
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    //  Cập nhật nhóm
    public Group update(Long id, Group newGroup) {
        return groupRepository.findById(id).map(existing -> {
            existing.setName(newGroup.getName());
            existing.setDescription(newGroup.getDescription());
            existing.setMembers(newGroup.getMembers());
            existing.setFund(newGroup.getFund());
            return groupRepository.save(existing);
        }).orElse(null);
    }

    //  Xóa nhóm
    public void delete(Long id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
        }
    }

    //  Lấy tổng quan tài chính của nhóm
    public Map<String, Object> getGroupSummary(Long groupId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            return null;
        }

        Group group = groupOpt.get();

        // Lấy tất cả chi tiêu và thu nhập liên quan đến nhóm
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Income> allIncomes = incomeRepository.findAll();

        double totalExpense = allExpenses.stream()
                .filter(e -> e.getMember() != null
                        && e.getMember().getGroup() != null
                        && e.getMember().getGroup().getId().equals(groupId))
                .mapToDouble(Expense::getAmount)
                .sum();

        double totalIncome = allIncomes.stream()
                .filter(i -> i.getMember() != null
                        && i.getMember().getGroup() != null
                        && i.getMember().getGroup().getId().equals(groupId))
                .mapToDouble(Income::getAmount)
                .sum();

        double balance = totalIncome - totalExpense;

        Map<String, Object> summary = new HashMap<>();
        summary.put("groupId", group.getId());
        summary.put("groupName", group.getName());
        summary.put("totalMembers", group.getMembers() != null ? group.getMembers().size() : 0);
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("balance", balance);

        return summary;
    }
}
