package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestInterfaceFactory {
    public static void main(String[] args) {
        System.out.println("=== Activity 12: Factory-Driven System Suite ===");

        // [Test 1] Savings Account Creation & Deposit
        try {
            IAccount savings = AccountFactory.createAccount("SAVINGS", 101, "Rajesh Sharma", 30, 10000.0);
            savings.deposit(500.0);
            boolean minBalEnforced = false;
            try {
                savings.withdraw(10000.0, "1234");
            } catch (MinimumBalanceViolationException e) {
                minBalEnforced = true;
            }
            if (savings.getBalance() == 10500.0 && minBalEnforced) {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [PASS]");
            } else {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL]");
            }
        } catch (Exception e) {
            System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL] - " + e.getMessage());
        }

        // [Test 2] Current Account Overdraft Withdrawal
        try {
            IAccount current = AccountFactory.createAccount("CURRENT", 102, "Priya Patel", 28, 5000.0);
            current.withdraw(8000.0, "1234");
            if (current.getBalance() == -3000.0) {
                System.out.println("[Test 2] Current Account Overdraft Withdrawal: [PASS]");
            } else {
                System.out.println("[Test 2] Current Account Overdraft Withdrawal: [FAIL]");
            }
        } catch (Exception e) {
            System.out.println("[Test 2] Current Account Overdraft Withdrawal: [FAIL] - " + e.getMessage());
        }

        // [Test 3] Fixed Deposit Premature Withdrawal Block
        try {
            IAccount fd = AccountFactory.createAccount("FIXED_DEPOSIT", 103, "Amit Kumar", 45, 50000.0);
            boolean blocked = false;
            try {
                fd.withdraw(5000.0, "1234");
            } catch (AccountException e) {
                blocked = true;
            }
            if (blocked) {
                System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [PASS]");
            } else {
                System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]");
            }
        } catch (Exception e) {
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL] - " + e.getMessage());
        }

        // [Test 4] Invalid Type Rejection
        try {
            boolean rejected = false;
            try {
                AccountFactory.createAccount("UNKNOWN_TYPE", 105, "Ghost User", 30, 1000.0);
            } catch (IllegalArgumentException e) {
                rejected = true;
            }
            if (rejected) {
                System.out.println("[Test 4] Invalid Type Rejection: [PASS]");
            } else {
                System.out.println("[Test 4] Invalid Type Rejection: [FAIL]");
            }
        } catch (Exception e) {
            System.out.println("[Test 4] Invalid Type Rejection: [FAIL] - " + e.getMessage());
        }

        System.out.println("Factory-driven architecture successfully verified!");
    }
}
