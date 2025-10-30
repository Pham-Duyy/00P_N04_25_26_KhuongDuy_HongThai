package com.fund.group09.Service;

import com.fund.group09.Model.Transaction;
import com.fund.group09.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // ✅ Lấy tất cả giao dịch
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // ✅ Tạo giao dịch mới
    public Transaction createTransaction(Transaction transaction) {
        transaction.setApproved(false); // Mặc định chưa duyệt
        transaction.setDate(LocalDateTime.now()); // Gán thời gian hiện tại
        return transactionRepository.save(transaction);
    }

    // ✅ Lấy giao dịch theo ID (dùng cho sửa)
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giao dịch với ID: " + id));
    }

    // ✅ Cập nhật giao dịch
    public Transaction updateTransaction(Transaction transaction) {
        // Có thể thêm kiểm tra tồn tại nếu cần
        return transactionRepository.save(transaction);
    }

    // ✅ Duyệt giao dịch
    public Transaction approveTransaction(Long id) {
        Transaction tx = findById(id);
        tx.setApproved(true);
        return transactionRepository.save(tx);
    }

    // ✅ Xóa giao dịch
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Không thể xóa. Giao dịch không tồn tại với ID: " + id);
        }
        transactionRepository.deleteById(id);
    }
}
