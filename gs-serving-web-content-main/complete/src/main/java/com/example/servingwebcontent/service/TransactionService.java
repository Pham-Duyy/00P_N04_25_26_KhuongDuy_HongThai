package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Transaction;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public Transaction addTransaction(Transaction transaction) {
        transactions.add(transaction);
        return transaction;
    }

    public Transaction updateTransaction(Long id, Transaction newTransaction) {
        for (Transaction transaction : transactions) {
            if (transaction.getId().equals(id)) {
                transaction.setAmount(newTransaction.getAmount());
                transaction.setDescription(newTransaction.getDescription());
                transaction.setDate(newTransaction.getDate());
                transaction.setCategory(newTransaction.getCategory());
                transaction.setMember(newTransaction.getMember());
                transaction.setType(newTransaction.getType());
                return transaction;
            }
        }
        return null;
    }

    public boolean deleteTransaction(Long id) {
        return transactions.removeIf(t -> t.getId().equals(id));
    }

    public Transaction findTransactionById(Long id) {
        for (Transaction t : transactions) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }
}
