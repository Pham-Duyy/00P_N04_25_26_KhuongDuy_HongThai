package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    // Lấy danh sách giao dịch thu cần đóng của user
    List<Transaction> findIncomeTransactionsForUser(Long userId);

    // Lấy danh sách giao dịch chi nhận thông báo của user
    List<Transaction> findExpenseNotificationsForUser(Long userId);

    // Xác nhận đã chuyển tiền cho giao dịch thu
    void confirmPayment(Long transactionId, Long userId);

    // Lấy danh sách giao dịch theo userId (nếu cần)
    List<Transaction> getTransactionsByUserId(Long userId);

    // Lấy danh sách giao dịch theo groupId
    List<Transaction> findByGroupId(Long groupId);

    // Lấy chi tiết giao dịch theo id
    Optional<Transaction> getTransactionById(Long id);

    // Thêm mới giao dịch
    Transaction save(Transaction transaction);

    // Lấy tất cả giao dịch
    List<Transaction> findAll();

    // Xóa giao dịch
    void deleteById(Long id);

    // Đếm tổng số giao dịch
    long countAll();

    // Đếm số giao dịch theo loại (INCOME/EXPENSE)
    long countByType(String type);
}