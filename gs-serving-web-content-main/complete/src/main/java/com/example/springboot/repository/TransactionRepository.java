package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Transaction;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {
    private List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> findAll() {
        return transactions;
    }

    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    public Transaction findById(Long id) {
        for (Transaction t : transactions) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    public boolean update(Long id, Transaction newTransaction) {
        Transaction transaction = findById(id);
        if (transaction != null) {
            transaction.setAmount(newTransaction.getAmount());
            transaction.setDescription(newTransaction.getDescription());
            transaction.setDate(newTransaction.getDate());
            transaction.setCategory(newTransaction.getCategory());
            transaction.setMember(newTransaction.getMember());
            transaction.setType(newTransaction.getType());
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        Transaction transaction = findById(id);
        if (transaction != null) {
            transactions.remove(transaction);
            return true;
        }
        return false;
    }
}
