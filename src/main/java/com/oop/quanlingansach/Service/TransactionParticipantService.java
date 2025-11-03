package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.TransactionParticipant;
import com.oop.quanlingansach.Repository.TransactionParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionParticipantService {

    @Autowired
    private TransactionParticipantRepository transactionParticipantRepository;

    public TransactionParticipant findByTransactionIdAndUserId(Long transactionId, Long userId) {
        return transactionParticipantRepository.findByTransactionIdAndUserId(transactionId, userId);
    }

    public List<TransactionParticipant> findByTransactionId(Long transactionId) {
        return transactionParticipantRepository.findByTransactionId(transactionId);
    }

    public List<TransactionParticipant> findByTransactionIdAndPaidTrue(Long transactionId) {
        return transactionParticipantRepository.findByTransactionIdAndPaidTrue(transactionId);
    }

    public TransactionParticipant save(TransactionParticipant participant) {
        return transactionParticipantRepository.save(participant);
    }

    // Bổ sung: Tìm participant theo id (dùng cho xác nhận thanh toán)
    public TransactionParticipant findById(Long id) {
        Optional<TransactionParticipant> optional = transactionParticipantRepository.findById(id);
        return optional.orElse(null);
    }

    // Tổng số tiền đã đóng góp thực tế (đã xác nhận) cho một nhóm
    public BigDecimal getTotalPaidAmountByGroup(Long groupId) {
        BigDecimal sum = transactionParticipantRepository.sumPaidAmountByGroup(groupId);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    // Lấy lịch sử đóng góp đã xác nhận của nhóm
    public List<TransactionParticipant> getPaidContributionsByGroup(Long groupId) {
        return transactionParticipantRepository.findByGroupIdAndPaidTrue(groupId);
    }
}