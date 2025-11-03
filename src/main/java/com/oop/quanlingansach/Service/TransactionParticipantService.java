package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.TransactionParticipant;
import com.oop.quanlingansach.Repository.TransactionParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}