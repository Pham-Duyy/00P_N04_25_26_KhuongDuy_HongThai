package com.fund.group09.Repository;

import com.fund.group09.Model.Transaction;
import com.fund.group09.Model.Transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //  Lấy danh sách giao dịch theo trạng thái duyệt
    List<Transaction> findByApproved(boolean approved);

    //  Lấy danh sách giao dịch theo loại (INCOME / EXPENSE)
    List<Transaction> findByType(TransactionType type);
}
