package com.oop.quanlingansach.Repository;

import com.oop.quanlingansach.Model.TransactionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionParticipantRepository extends JpaRepository<TransactionParticipant, Long> {
    // Lấy tất cả participant theo transactionId
    List<TransactionParticipant> findByTransactionId(Long transactionId);

    // Lấy tất cả participant đã thanh toán theo transactionId
    List<TransactionParticipant> findByTransactionIdAndPaidTrue(Long transactionId);

    // Đếm số participant đã thanh toán
    long countByPaidTrue();

    // Lấy participant theo transactionId và userId
    @Query("SELECT tp FROM TransactionParticipant tp WHERE tp.transaction.id = :transactionId AND tp.user.id = :userId")
    TransactionParticipant findByTransactionIdAndUserId(Long transactionId, Long userId);

    // Lấy tất cả participant theo userId
    List<TransactionParticipant> findByUserId(Long userId);

    // Lấy tất cả đóng góp đã thanh toán của user
    List<TransactionParticipant> findByUserIdAndPaidTrue(Long userId);

    // Lấy tất cả participant theo groupId (thông qua transaction)
    @Query("SELECT tp FROM TransactionParticipant tp WHERE tp.transaction.group.id = :groupId")
    List<TransactionParticipant> findByGroupId(Long groupId);

    // Tổng số tiền đã đóng góp thực tế (đã xác nhận) cho một nhóm
    @Query("SELECT COALESCE(SUM(tp.amount), 0) FROM TransactionParticipant tp WHERE tp.transaction.group.id = :groupId AND tp.paid = true")
    BigDecimal sumPaidAmountByGroup(Long groupId);

    // Lấy tất cả đóng góp đã xác nhận của nhóm
    @Query("SELECT tp FROM TransactionParticipant tp WHERE tp.transaction.group.id = :groupId AND tp.paid = true")
    List<TransactionParticipant> findByGroupIdAndPaidTrue(Long groupId);
}