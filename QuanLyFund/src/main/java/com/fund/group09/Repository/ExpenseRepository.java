package com.fund.group09.Repository;

import com.fund.group09.Model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByMemberId(Long memberId);
}
