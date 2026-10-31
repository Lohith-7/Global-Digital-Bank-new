package com.gdb.tests;

import com.gdb.command.TransactionCommand;
import com.gdb.domain.*;
import com.gdb.logging.*;
import com.gdb.service.AccountService;

import java.util.List;

public class TestAccountService {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 19 \u2014 ACCOUNT SERVICE DEMO");
        System.out.println("============================================================\n");

        try {
            // 📝 STEP 12 — Wire dependencies
            MemoryLogDestination memDest = new MemoryLogDestination();
            TransactionLogger logger = new TransactionLogger(memDest);
            AccountService service = new AccountService(logger);

            // 📝 STEP 13 — Open accounts
            IAccount acc1 = service.openAccount("SAVINGS", "John Doe", 25, 15000.0);
            ((AbstractAccount) acc1).setPin(1234);
            IAccount acc2 = service.openAccount("SAVINGS", "Jane Smith", 30, 10000.0);
            ((AbstractAccount) acc2).setPin(1234);

            System.out.println("[STEP 13] Opened: " + acc1);
            System.out.println("[STEP 13] Opened: " + acc2);

            // 📝 STEP 14 — Deposit
            Transaction depositTxn = service.deposit(acc1.getAccountNumber(), 5000);
            System.out.println("\n[STEP 14] Deposit: " + depositTxn);

            // 📝 STEP 15 — Withdrawal
            Transaction withdrawTxn = service.withdraw(acc1.getAccountNumber(), 2000, 1234);
            System.out.println("[STEP 15] Withdrawal: " + withdrawTxn);

            // 📝 STEP 16 — Transfer
            Transaction transferTxn = service.transfer(acc1.getAccountNumber(), acc2.getAccountNumber(), 1000, 1234);
            System.out.println("[STEP 16] Transfer: " + transferTxn);

            // 📝 STEP 17 — Final Balances
            System.out.println("\n[STEP 17] Final Balances:");
            System.out.println("  John (Account #" + acc1.getAccountNumber() + "): Rs. " + acc1.getBalance());
            System.out.println("  Jane (Account #" + acc2.getAccountNumber() + "): Rs. " + acc2.getBalance());

            // 📝 STEP 18 — Transaction History
            List<TransactionCommand> history = service.getTransactionHistory();
            System.out.println("\n[STEP 18] Transaction History (" + history.size() + " records):");
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
            }

            // 📝 STEP 19 — Error Handling Checks
            System.out.println("\n[STEP 19] Error Handling Checks:");

            // Deposit to missing account
            try {
                service.deposit(9999, 1000);
                System.out.println("  Deposit to missing account: Expected exception [FAIL]");
            } catch (Exception e) {
                System.out.println("  Deposit to missing account caught: " + e.getMessage() + " [PASS]");
            }

            // Withdrawal with wrong PIN
            try {
                service.withdraw(acc1.getAccountNumber(), 500, 9999);
                System.out.println("  Withdrawal with wrong PIN: Expected exception [FAIL]");
            } catch (Exception e) {
                System.out.println("  Withdrawal with wrong PIN caught: " + e.getMessage() + " [PASS]");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
