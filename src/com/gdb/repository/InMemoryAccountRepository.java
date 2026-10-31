package com.gdb.repository;

import com.gdb.domain.IAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<Integer, IAccount> accounts = new HashMap<>();
    private int nextAccountNumber = 1001;

    @Override
    public void save(IAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        int accountNumber = account.getAccountNumber();
        if (accountNumber <= 0) {
            accountNumber = nextAccountNumber;
            if (account instanceof com.gdb.domain.AbstractAccount) {
                ((com.gdb.domain.AbstractAccount) account).setAccountNumber(accountNumber);
            }
        }

        if (accountNumber >= nextAccountNumber) {
            nextAccountNumber = accountNumber + 1;
        }

        accounts.put(accountNumber, account);
    }

    @Override
    public IAccount findById(int accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public java.util.Collection<IAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void update(IAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public void delete(int accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public boolean exists(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    @Override
    public int nextAccountNumber() {
        return nextAccountNumber;
    }
}
