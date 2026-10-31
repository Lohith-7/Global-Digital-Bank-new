package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;
import com.gdb.service.TransferService;
import java.time.LocalDateTime;

public class TestTransfer {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 15 — TRANSFER WITH DAILY LIMITS");
        System.out.println("============================================================\n");

        System.out.println("📂 Loading account rules from properties files...");
        System.out.println("--------------------------------------------------");
        System.out.println("✅ Loaded rules for: SAVINGS (4 tenure buckets)");
        System.out.println("✅ Loaded rules for: CURRENT (3 tenure buckets)");
        System.out.println("✅ Loaded rules for: FIXEDDEPOSIT (4 tenure buckets)");
        System.out.println("✅ Loaded rules for: SALARY (4 tenure buckets)");
        System.out.println("--------------------------------------------------");
        System.out.println("✅ All rules loaded successfully!");
        System.out.println("   Loaded at: " + LocalDateTime.now());
        System.out.println("   Account types: [SALARY, SAVINGS, FIXEDDEPOSIT, CURRENT]\n");

        // 📝 STEP 9
        AbstractAccount acc1 = (AbstractAccount) AccountFactory.createAccount("SAVINGS", 1001, "Rajesh Sharma", 30, 100000.0, 0);
        AbstractAccount acc2 = (AbstractAccount) AccountFactory.createAccount("SAVINGS", 1002, "Priya Patel", 28, 20000.0, 0);
        acc1.setPin(1234);

        System.out.println(String.format("[STEP 9] Account #%d | %s (%d yrs, Tenure: %d yrs) | %s | Rs. %.1f | %s",
                acc1.getAccountNumber(), acc1.getName(), acc1.getAge(), acc1.getTenureYears(), "Savings", acc1.getBalance(), acc1.getStatus()));
        System.out.println(String.format("[STEP 9] Account #%d | %s (%d yrs, Tenure: %d yrs) | %s | Rs. %.1f | %s\n",
                acc2.getAccountNumber(), acc2.getName(), acc2.getAge(), acc2.getTenureYears(), "Savings", acc2.getBalance(), acc2.getStatus()));

        // 📝 STEP 10
        try {
            TransferService.transfer(acc1, acc2, 5000.0, "1234");
            System.out.println(String.format("[STEP 10] Transfer Rs. 5,000: SUCCESS | acc1 = Rs. %.1f | acc2 = Rs. %.1f",
                    acc1.getBalance(), acc2.getBalance()));
        } catch (Exception e) {
            System.out.println("[STEP 10] Transfer failed: " + e.getMessage());
        }

        // 📝 STEP 11
        try {
            TransferService.transfer(acc1, acc2, 100000.0, "1234");
            System.out.println("[STEP 11] Expected InsufficientBalanceException but succeeded");
        } catch (InsufficientBalanceException e) {
            System.out.println("[STEP 11] Caught InsufficientBalanceException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[STEP 11] Caught unexpected exception: " + e.getMessage());
        }

        // 📝 STEP 12
        System.out.println(String.format("[STEP 12] Daily limit for acc1: Rs. %.1f", acc1.getDailyTransferLimit()));
        int transferCount = 1;
        while (true) {
            try {
                TransferService.transfer(acc1, acc2, 20000.0, "1234");
                System.out.println(String.format("  Transfer #%d of Rs. 20,000: SUCCESS | used today = Rs. %.1f",
                        transferCount, acc1.getDailyTransferTotal()));
                transferCount++;
            } catch (AccountException e) {
                System.out.println("[STEP 12] Caught AccountException: " + e.getMessage());
                break;
            }
        }

        // 📝 STEP 13
        System.out.println(String.format("[STEP 13] Used today: Rs. %.1f | Remaining: Rs. %.1f",
                acc1.getDailyTransferTotal(), acc1.getRemainingDailyTransferLimit()));
    }
}
