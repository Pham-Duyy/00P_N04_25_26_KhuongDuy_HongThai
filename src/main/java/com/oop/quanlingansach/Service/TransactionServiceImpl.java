package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.Transaction;
import com.oop.quanlingansach.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<Transaction> findByGroupId(Long groupId) {
        return transactionRepository.findByGroupId(groupId);
    }

    @Override
    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByCreatedById(userId);
    }

    // Lấy danh sách giao dịch thu cần đóng của user
    @Override
    public List<Transaction> findIncomeTransactionsForUser(Long userId) {
        return transactionRepository.findIncomeTransactionsForUser(userId);
    }

    // Lấy danh sách giao dịch chi nhận thông báo của user
    @Override
    public List<Transaction> findExpenseNotificationsForUser(Long userId) {
        return transactionRepository.findExpenseNotificationsForUser(userId);
    }

    // Xác nhận đã chuyển tiền cho giao dịch thu
    @Override
    public void confirmPayment(Long transactionId, Long userId) {
        // TODO: Cập nhật trạng thái xác nhận thanh toán cho user với transactionId
        // Tùy vào thiết kế bảng dữ liệu của bạn
    }
}