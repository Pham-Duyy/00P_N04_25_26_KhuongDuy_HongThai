package com.fund.group09.Repository;

import com.fund.group09.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByApproved(Boolean approved);
    List<Transaction> findByType(Transaction.TransactionType type);
}
