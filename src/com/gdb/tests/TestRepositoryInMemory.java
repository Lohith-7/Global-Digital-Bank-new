package com.gdb.tests;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;
import com.gdb.repository.AccountRepository;
import com.gdb.repository.RepositoryFactory;
import com.gdb.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TestRepositoryInMemory {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 21 — REPOSITORY PATTERN (IN MEMORY)");
        System.out.println("============================================================\n");

        try {
            AccountRepository accountRepository = RepositoryFactory.getAccountRepository();
            TransactionRepository transactionRepository = RepositoryFactory.getTransactionRepository();

            System.out.println("Persistence mode: " + RepositoryFactory.getPersistenceMode());

            int accountNumber = accountRepository.nextAccountNumber();
            IAccount account = AccountFactory.createAccount("SAVINGS", accountNumber, "Alice Johnson", 25, 12000.0, 0);
            ((AbstractAccount) account).setPin(1234);
            accountRepository.save(account);
            check(accountRepository.exists(account.getAccountNumber()), "Account saved successfully.");

            IAccount loaded = accountRepository.findById(account.getAccountNumber());
            check(loaded != null && loaded.getCustomerName().equals("Alice Johnson"), "Account retrieved by ID.");

            account.deposit(1500.0);
            accountRepository.update(account);
            IAccount updated = accountRepository.findById(account.getAccountNumber());
            check(updated != null && updated.getBalance() == 13500.0, "Account updated successfully.");

            Transaction depositTxn = new Transaction(
                    "TXN-001",
                    LocalDateTime.now(),
                    account.getAccountNumber(),
                    TransactionType.DEPOSIT,
                    1500.0,
                    account.getBalance(),
                    "SUCCESS",
                    "Initial deposit",
                    0,
                    0
            );
            transactionRepository.save(depositTxn);

            List<Transaction> history = transactionRepository.findByAccount(account.getAccountNumber());
            check(history.size() == 1, "Transaction found for account.");

            accountRepository.delete(account.getAccountNumber());
            check(!accountRepository.exists(account.getAccountNumber()), "Account deleted successfully.");

            System.out.println("\nAll repository tests passed.");
        } catch (Exception e) {
            System.out.println("Repository test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
        System.out.println("[PASS] " + message);
    }
}
