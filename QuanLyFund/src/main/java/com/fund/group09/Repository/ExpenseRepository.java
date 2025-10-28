package com.fund.group09.Repository;

import com.fund.group09.Model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    // Tìm chi tiêu theo member
    List<Expense> findByMemberId(Long memberId);
    
    // Tính tổng chi tiêu của member
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.member.id = :memberId")
    Double sumAmountByMemberId(@Param("memberId") Long memberId);
    
    // Tìm chi tiêu theo group
    @Query("SELECT e FROM Expense e WHERE e.member.group.id = :groupId")
    List<Expense> findByGroupId(@Param("groupId") Long groupId);
    
    // Tính tổng chi tiêu của group
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.member.group.id = :groupId")
    Double sumAmountByGroupId(@Param("groupId") Long groupId);
    
    // Tìm chi tiêu theo member và khoảng thời gian
    @Query("SELECT e FROM Expense e WHERE e.member.id = :memberId " +
           "AND e.date BETWEEN :startDate AND :endDate")
    List<Expense> findByMemberIdAndDateBetween(
        @Param("memberId") Long memberId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}