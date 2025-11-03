package com.oop.quanlingansach.Repository;

import com.oop.quanlingansach.Model.TransactionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionParticipantRepository extends JpaRepository<TransactionParticipant, Long> {
    // Lấy tất cả participant theo transactionId
    List<TransactionParticipant> findByTransactionId(Long transactionId);

    // Lấy tất cả participant đã thanh toán theo transactionId
    List<TransactionParticipant> findByTransactionIdAndPaidTrue(Long transactionId);

    // Lấy participant theo transactionId và userId
    @Query("SELECT tp FROM TransactionParticipant tp WHERE tp.transaction.id = :transactionId AND tp.user.id = :userId")
    TransactionParticipant findByTransactionIdAndUserId(Long transactionId, Long userId);

    // Lấy tất cả đóng góp đã thanh toán của user
    List<TransactionParticipant> findByUserIdAndPaidTrue(Long userId);
}