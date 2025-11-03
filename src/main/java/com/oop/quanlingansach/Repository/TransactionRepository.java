package com.oop.quanlingansach.Repository;

import com.oop.quanlingansach.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByGroupId(Long groupId);
    List<Transaction> findByCreatedById(Long userId);

    // Giao dịch thu cần đóng của user (user thuộc group của transaction và type = 'INCOME')
    @Query("SELECT t FROM Transaction t JOIN t.group g JOIN g.members m WHERE m.id = :userId AND t.type = 'INCOME'")
    List<Transaction> findIncomeTransactionsForUser(Long userId);

    // Giao dịch chi nhận thông báo của user (user thuộc group và type = 'EXPENSE')
    @Query("SELECT t FROM Transaction t JOIN t.group g JOIN g.members m WHERE m.id = :userId AND t.type = 'EXPENSE'")
    List<Transaction> findExpenseNotificationsForUser(Long userId);

    // Đếm số giao dịch theo loại (INCOME/EXPENSE)
    long countByType(String type);

    // Tổng số tiền đã chi (từ các giao dịch chi của nhóm)
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.group.id = :groupId AND t.type = 'EXPENSE'")
    BigDecimal sumExpenseByGroup(Long groupId);

    // Lấy lịch sử các giao dịch chi của nhóm (mới nhất trước)
    @Query("SELECT t FROM Transaction t WHERE t.group.id = :groupId AND t.type = 'EXPENSE' ORDER BY t.createdDate DESC")
    List<Transaction> findExpensesByGroup(Long groupId);
}