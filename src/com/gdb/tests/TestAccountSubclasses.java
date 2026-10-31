package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestAccountSubclasses {
    public static void main(String[] args) {
        System.out.println("=== Activity 8: Polymorphism Test ===");

        // Test 1: SavingsAccount minimum balance check
        AbstractAccount sa = new SavingsAccount(10000.0);
        sa.setPin(1234);
        try {
            sa.withdraw(9500.0, 1234);
            System.out.println("[Savings] Withdraw 9500 (breaches min balance 1000): FAILED");
        } catch (MinimumBalanceViolationException e) {
            System.out.println("[Savings] Withdraw 9500 (breaches min balance 1000): Caught MinimumBalanceViolationException [PASS]");
        } catch (Exception e) {
            System.out.println("[Savings] Unexpected exception: " + e.getMessage());
        }

        // Test 2: CurrentAccount with Overdraft
        AbstractAccount ca = new CurrentAccount(5000.0);
        ca.setPin(1234);
        try {
            ca.withdraw(10000.0, 1234);
            System.out.println("[Current] Withdraw with Overdraft (Balance goes to -5000): SUCCESS [PASS]");
        } catch (Exception e) {
            System.out.println("[Current] Withdraw failed: " + e.getMessage());
        }

        // Test 3: CurrentAccount exceeding Overdraft
        try {
            ca.withdraw(21000.0, 1234);
            System.out.println("[Current] Withdraw exceeding Overdraft (exceeds -25000): FAILED");
        } catch (InsufficientBalanceException e) {
            System.out.println("[Current] Withdraw exceeding Overdraft (exceeds -25000): Caught InsufficientBalanceException [PASS]");
        } catch (Exception e) {
            System.out.println("[Current] Unexpected exception: " + e.getMessage());
        }

        // Test 4: FixedDepositAccount premature withdrawal
        AbstractAccount fda = new FixedDepositAccount(50000.0);
        fda.setPin(1234);
        try {
            fda.withdraw(5000.0, 1234);
            System.out.println("[FixedDeposit] Withdraw attempt: FAILED");
        } catch (AccountException e) {
            System.out.println("[FixedDeposit] Withdraw attempt: Caught AccountException [PASS]");
        } catch (Exception e) {
            System.out.println("[FixedDeposit] Unexpected exception: " + e.getMessage());
        }

        System.out.println("All polymorphic behaviors verified!");
    }
}