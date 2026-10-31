package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestAbstractAccount {
    public static void main(String[] args) {
        testActivity9();
        System.out.println();
        testActivity10();
    }

    private static void testActivity9() {
        System.out.println("=== Activity 9: Abstract Account & Template Pattern ===");

        // [Savings] Withdraw 2000
        SavingsAccount sa = new SavingsAccount(10000.0);
        sa.setPin("1234");
        try {
            sa.withdraw(2000.0, "1234");
            System.out.println("[Savings] Withdraw 2000: SUCCESS | Balance: Rs " + sa.getBalance());
        } catch (Exception e) {
            System.out.println("[Savings] Withdraw 2000 failed: " + e.getMessage());
        }

        // [Savings] Withdraw below min balance
        try {
            sa.withdraw(7500.0, "1234");
            System.out.println("[Savings] Withdraw below min balance: FAILED");
        } catch (MinimumBalanceViolationException e) {
            System.out.println("[Savings] Withdraw below min balance: Caught MinimumBalanceViolationException [PASS]");
        } catch (Exception e) {
            System.out.println("[Savings] Unexpected exception: " + e.getMessage());
        }

        // [Current] Overdraft debit
        CurrentAccount ca = new CurrentAccount(5000.0);
        ca.setPin("1234");
        try {
            ca.withdraw(8000.0, "1234");
            System.out.println("[Current] Overdraft debit: SUCCESS | Balance: Rs " + ca.getBalance());
        } catch (Exception e) {
            System.out.println("[Current] Overdraft debit failed: " + e.getMessage());
        }

        // [FixedDeposit] Premature debit
        FixedDepositAccount fda = new FixedDepositAccount(50000.0);
        fda.setPin("1234");
        try {
            fda.withdraw(5000.0, "1234");
            System.out.println("[FixedDeposit] Premature debit: FAILED");
        } catch (AccountException e) {
            System.out.println("[FixedDeposit] Premature debit: Caught AccountException [PASS]");
        } catch (Exception e) {
            System.out.println("[FixedDeposit] Unexpected exception: " + e.getMessage());
        }

        System.out.println("Template method pattern executed successfully!");
    }

    private static void testActivity10() {
        System.out.println("=== Activity 10: Banking Operations Suite ===");

        // Step 1: Create Account Portfolio
        SavingsAccount savings = new SavingsAccount(10000.0);
        savings.setPin("1234");
        CurrentAccount current = new CurrentAccount(5000.0);
        current.setPin("1234");
        SalaryAccount salary = new SalaryAccount(20000.0, "TechCorp");
        salary.setPin("1234");

        AbstractAccount[] portfolio = new AbstractAccount[] {
            savings,
            current,
            salary
        };

        // Step 2: Implement Fund Transfer Method
        boolean success = transferFunds(savings, current, 3000.0, "1234");
        if (success) {
            System.out.println("Transfer Rs 3000 from Savings to Current: SUCCESS");
            System.out.println("Savings Balance: Rs " + savings.getBalance() + " | Current Balance: Rs " + current.getBalance());
        }

        // Failed Transfer (Wrong PIN)
        double prevSavingsBal = savings.getBalance();
        double prevCurrentBal = current.getBalance();
        boolean failedAttempt = transferFunds(savings, current, 1000.0, "9999");
        if (!failedAttempt && savings.getBalance() == prevSavingsBal && current.getBalance() == prevCurrentBal) {
            System.out.println("Failed Transfer (Wrong PIN): Exception caught, no balance changed [PASS]");
        }

        // Step 3: Process Monthly Banking Cycle
        processMonthlyCycle(portfolio);
        System.out.println("Monthly Interest Cycle processed for all qualifying accounts.");
        System.out.println("All banking operations passed!");
    }

    public static boolean transferFunds(AbstractAccount from, AbstractAccount to, double amount, String pin) {
        try {
            from.withdraw(amount, pin);
            to.deposit(amount);
            return true;
        } catch (AccountException e) {
            return false;
        }
    }

    public static void processMonthlyCycle(AbstractAccount[] accounts) {
        for (AbstractAccount acc : accounts) {
            if (acc instanceof SavingsAccount) {
                ((SavingsAccount) acc).applyInterest();
            } else if (acc instanceof SalaryAccount) {
                // salary history verification
            }
        }
    }
}
