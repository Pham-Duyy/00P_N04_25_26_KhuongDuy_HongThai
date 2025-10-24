package com.fund.group09.Service;

import com.fund.group09.Model.Member;
import com.fund.group09.Model.Expense;
import com.fund.group09.Model.Income;
import com.fund.group09.Repository.MemberRepository;
import com.fund.group09.Repository.ExpenseRepository;
import com.fund.group09.Repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    //  Lấy tất cả thành viên
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    //  Lấy thành viên theo ID
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    //  Thêm thành viên mới
    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    //  Cập nhật thông tin thành viên
    public Optional<Member> updateMember(Long id, Member newMember) {
        return memberRepository.findById(id).map(member -> {
            member.setName(newMember.getName());
            member.setEmail(newMember.getEmail());
            member.setRole(newMember.getRole());
            member.setGroup(newMember.getGroup());
            return memberRepository.save(member);
        });
    }

    //  Xóa thành viên
    public boolean deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            return false;
        }
        memberRepository.deleteById(id);
        return true;
    }

    //  Lấy danh sách thành viên theo Group ID
    public List<Member> getMembersByGroup(Long groupId) {
        return memberRepository.findByGroupId(groupId);
    }

    //  Tính toán tổng thu, chi và số dư của từng thành viên
    public Map<String, Object> getMemberSummary(Long id) {
        Optional<Member> memberOpt = memberRepository.findById(id);
        if (memberOpt.isEmpty()) {
            return null;
        }

        Member member = memberOpt.get();

        List<Expense> expenses = expenseRepository.findByMemberId(id);
        List<Income> incomes = incomeRepository.findByMemberId(id);

        BigDecimal totalExpense = expenses.stream()
                .map(e -> BigDecimal.valueOf(e.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIncome = incomes.stream()
                .map(i -> BigDecimal.valueOf(i.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("memberId", member.getId());
        summary.put("memberName", member.getName());
        summary.put("email", member.getEmail());
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("balance", balance);

        return summary;
    }
}
