package com.fund.group09.Repository;

import com.fund.group09.Model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    
    // Tìm thu nhập theo member
    List<Income> findByMemberId(Long memberId);
    
    // Tính tổng thu nhập của member
    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.member.id = :memberId")
    Double sumAmountByMemberId(@Param("memberId") Long memberId);
    
    // Tìm thu nhập theo group
    @Query("SELECT i FROM Income i WHERE i.member.group.id = :groupId")
    List<Income> findByGroupId(@Param("groupId") Long groupId);
    
    // Tính tổng thu nhập của group
    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.member.group.id = :groupId")
    Double sumAmountByGroupId(@Param("groupId") Long groupId);
    
    // Tìm thu nhập theo member và khoảng thời gian
    @Query("SELECT i FROM Income i WHERE i.member.id = :memberId " +
           "AND i.date BETWEEN :startDate AND :endDate")
    List<Income> findByMemberIdAndDateBetween(
        @Param("memberId") Long memberId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}