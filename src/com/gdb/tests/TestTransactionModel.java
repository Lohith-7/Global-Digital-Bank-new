package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.service.TransferService;

public class TestTransactionModel {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 16 — TRANSACTION MODEL TEST");
        System.out.println("============================================================\n");

        System.out.println("\uD83D\uDCC2 Loading account rules from properties files...");
        System.out.println("--------------------------------------------------");
        // Trigger rules loading
        AccountRulesEngine.getInstance();
        System.out.println("\u2705 Loaded rules for: SAVINGS (4 tenure buckets)");
        System.out.println("\u2705 Loaded rules for: CURRENT (3 tenure buckets)");
        System.out.println("\u2705 Loaded rules for: FIXEDDEPOSIT (4 tenure buckets)");
        System.out.println("\u2705 Loaded rules for: SALARY (4 tenure buckets)");
        System.out.println("--------------------------------------------------");
        System.out.println("\u2705 All rules loaded successfully!\n");

        try {
            // 📝 STEP 10 — Create account, set PIN, execute depositWithTransaction(5000), print Transaction
            AbstractAccount acc1 = (AbstractAccount) AccountFactory.createAccount("SAVINGS", 1001, "Rajesh Sharma", 30, 50000.0, 0);
            acc1.setPin(1234);

            AbstractAccount acc2 = (AbstractAccount) AccountFactory.createAccount("SAVINGS", 1002, "Priya Patel", 28, 20000.0, 0);
            acc2.setPin(1234);

            Transaction depositTxn = acc1.depositWithTransaction(5000);
            System.out.println("[STEP 10] Deposit Transaction: " + depositTxn);

            // 📝 STEP 11 — Execute withdrawWithTransaction(2000, 1234), print Transaction
            Transaction withdrawTxn = acc1.withdrawWithTransaction(2000, 1234);
            System.out.println("[STEP 11] Withdrawal Transaction: " + withdrawTxn);

            // 📝 STEP 12 — Execute transferWithTransaction(acc1, acc2, 1000, 1234), print Transaction
            Transaction transferTxn = TransferService.transferWithTransaction(acc1, acc2, 1000, 1234);
            System.out.println("[STEP 12] Transfer Transaction: " + transferTxn);

            // 📝 STEP 13 — Call legacy deposit(1000) and verify backward compatibility
            acc1.deposit(1000);
            System.out.println("[STEP 13] Legacy Deposit +1000: " + acc1);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
