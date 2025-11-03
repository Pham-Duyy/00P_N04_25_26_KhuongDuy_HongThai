package com.oop.quanlingansach.Repository;

import com.oop.quanlingansach.Model.TransactionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionParticipantRepository extends JpaRepository<TransactionParticipant, Long> {
    List<TransactionParticipant> findByTransactionId(Long transactionId);

    List<TransactionParticipant> findByTransactionIdAndPaidTrue(Long transactionId);

    @Query("SELECT tp FROM TransactionParticipant tp WHERE tp.transaction.id = :transactionId AND tp.user.id = :userId")
    TransactionParticipant findByTransactionIdAndUserId(Long transactionId, Long userId);
}