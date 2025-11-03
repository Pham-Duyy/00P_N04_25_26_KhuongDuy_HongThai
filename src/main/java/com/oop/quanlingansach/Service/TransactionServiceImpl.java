package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.Transaction;
import com.oop.quanlingansach.Model.TransactionParticipant;
import com.oop.quanlingansach.Repository.TransactionRepository;
import com.oop.quanlingansach.Repository.TransactionParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionParticipantRepository transactionParticipantRepository;

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

    @Override
    public List<Transaction> findIncomeTransactionsForUser(Long userId) {
        return transactionRepository.findIncomeTransactionsForUser(userId);
    }

    @Override
    public List<Transaction> findExpenseNotificationsForUser(Long userId) {
        return transactionRepository.findExpenseNotificationsForUser(userId);
    }

    @Override
    public void confirmPayment(Long transactionId, Long userId) {
        // TODO: Cập nhật trạng thái xác nhận thanh toán cho user với transactionId
        // Tùy vào thiết kế bảng dữ liệu của bạn
    }

    @Override
    public long countAll() {
        return transactionRepository.count();
    }

    @Override
    public long countByType(String type) {
        return transactionRepository.countByType(type);
    }

    // Lấy danh sách đóng góp đã thanh toán của user
    @Override
    public List<TransactionParticipant> findPaidContributionsByUserId(Long userId) {
        return transactionParticipantRepository.findByUserIdAndPaidTrue(userId);
    }

    // Lấy tất cả đóng góp (cả đã đóng và chưa đóng) của user
    @Override
    public List<TransactionParticipant> findAllContributionsByUserId(Long userId) {
        return transactionParticipantRepository.findByUserId(userId);
    }

    // Tổng số tiền đã chi (từ các giao dịch chi của nhóm)
    @Override
    public BigDecimal getTotalExpenseByGroup(Long groupId) {
        BigDecimal sum = transactionRepository.sumExpenseByGroup(groupId);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    // Lấy lịch sử các giao dịch chi của nhóm
    @Override
    public List<Transaction> getExpenseHistoryByGroup(Long groupId) {
        return transactionRepository.findExpensesByGroup(groupId);
    }
}