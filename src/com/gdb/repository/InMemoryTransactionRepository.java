package com.gdb.repository;

import com.gdb.domain.Transaction;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionRepository implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findByAccount(int accountNumber) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber() == accountNumber
                    || transaction.getFromAccount() == accountNumber
                    || transaction.getToAccount() == accountNumber) {
                result.add(transaction);
            }
        }
        return result;
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    @Override
    public void clear() {
        transactions.clear();
    }
}
