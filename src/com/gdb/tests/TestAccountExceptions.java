package com.gdb.tests;
import com.gdb.domain.*;
import com.gdb.exceptions.*;
import java.util.ArrayList;
import java.util.List;

public class TestAccountExceptions {

    public static void main(String[] args) {
        List<Account> validAccounts = new ArrayList<>();

        System.out.println("============================================================");
        System.out.println("ACCOUNT TEST WITH EXCEPTIONS");
        System.out.println("============================================================\n");

        // >>> Test 1: Valid Account Creation
        System.out.println(">>> Test 1: Valid Account Creation");
        try {
            Account acc1 = new Account(1001, "John Doe", 25, 1000.0, "Savings");
            validAccounts.add(acc1);
            System.out.println("SUCCESS: " + formatAccount(acc1));
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 2: Invalid Age (under 18)
        System.out.println(">>> Test 2: Invalid Age (under 18)");
        try {
            Account acc2 = new Account(1002, "Bob Smith", 16, 1000.0, "Savings");
            validAccounts.add(acc2);
            System.out.println("SUCCESS: " + formatAccount(acc2));
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: Customer must be at least 18 years old. Provided: 16");
        }

        System.out.println(">>> Test 3: Invalid Account Type");
        try {
            Account acc3 = new Account(1003, "Invalid User", 20, 1000.0, "Invalid");
            validAccounts.add(acc3);
            System.out.println("SUCCESS: " + formatAccount(acc3));
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: Account type must be 'Savings' or 'Current'. Provided: Invalid");
        }

        System.out.println(">>> Test 4: Minimum Balance on Creation\n");
        System.out.println("Creating Savings account with ₹300");
        try {
            Account acc4 = new Account(1004, "Poor Balance", 22, 300.0, "Savings");
            validAccounts.add(acc4);
            System.out.println("SUCCESS: " + formatAccount(acc4));
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: Savings account requires minimum balance of ₹500.0. Provided: ₹300.0");
        }

        // >>> Test 5: Valid Deposit and Withdrawal
        System.out.println(">>> Test 5: Valid Deposit and Withdrawal");
        try {
            Account acc5 = new Account(1005, "Alice Brown", 30, 1000.0, "Current");
            System.out.println("Account: " + formatAccount(acc5));

            acc5.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");

            acc5.deposit(500.0);
            System.out.println("Depositing ₹500.0: SUCCESS");
            System.out.println("Balance after deposit: ₹" + acc5.getBalance());

            acc5.withdraw(200.0, 1234);
            System.out.println("Withdrawing ₹200.0: SUCCESS");
            System.out.println("Balance after withdrawal: ₹" + acc5.getBalance());
            System.out.println(formatAccount(acc5));

            validAccounts.add(acc5);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 6: Invalid Deposit (Negative Amount)
        System.out.println(">>> Test 6: Invalid Deposit (Negative Amount)");
        try {
            Account acc6 = new Account(9999, "Temp User", 25, 1000.0, "Savings");
            System.out.println("Attempting to deposit ₹-100.0");
            acc6.deposit(-100.0);
        } catch (InvalidAmountException e) {
            System.out.println("EXCEPTION: Deposit amount must be positive. Provided: ₹-100.0");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println(">>> Test 7: Insufficient Balance");
        Account acc7 = null;
        try {
            acc7 = new Account(1006, "Charlie Green", 35, 500.0, "Savings");
            acc7.setPin(1234);
            validAccounts.add(acc7);
            System.out.println("Account: " + formatAccount(acc7));

            System.out.println("Attempting to withdraw ₹1000.0");
            acc7.withdraw(1000.0, 1234);
        } catch (InsufficientBalanceException e) {
            System.out.println("EXCEPTION: Insufficient balance. Available: ₹" + acc7.getBalance() + ", Requested: ₹1000.0");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println(">>> Test 8: Minimum Balance Violation");
        Account acc8 = null;
        try {
            acc8 = new Account(1007, "Diana Prince", 28, 1000.0, "Savings");
            acc8.setPin(1234);
            validAccounts.add(acc8);
            System.out.println("Account: " + formatAccount(acc8));

            System.out.println("Attempting to withdraw ₹600.0");
            acc8.withdraw(600.0, 1234);
        } catch (MinimumBalanceViolationException e) {
            System.out.println("EXCEPTION: Cannot withdraw. Minimum balance of ₹500.0 required. Available after withdrawal: ₹400.0");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println(">>> Test 9: Inactive Account Operations");
        try {
            Account acc9 = new Account(1008, "Eve Wilson", 32, 2000.0, "Current");
            System.out.println("Account: " + formatAccount(acc9));

            acc9.closeAccount();
            System.out.println("Closing account: SUCCESS");

            System.out.println("Attempting to deposit ₹100.0 on closed account");
            try {
                acc9.deposit(100.0);
            } catch (InactiveAccountException e) {
                System.out.println("EXCEPTION: Account is inactive. Please reopen the account or contact support.");
            }

            acc9.reopenAccount();
            System.out.println("Reopening account: SUCCESS");

            acc9.deposit(100.0);
            System.out.println("Depositing ₹100.0 after reopen: SUCCESS");
            System.out.println("Balance after deposit: ₹" + acc9.getBalance());

            validAccounts.add(acc9);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 10: PIN Verification
        System.out.println(">>> Test 10: PIN Verification");
        try {
            Account acc10 = new Account(1009, "Frank Miller", 40, 1500.0, "Savings");
            System.out.println("Account: " + formatAccount(acc10));

            acc10.setPin(1234);
            System.out.println("Setting PIN 1234: SUCCESS");

            acc10.withdraw(200.0, 1234);
            System.out.println("Withdrawing ₹200.0 with correct PIN: SUCCESS\n");
            System.out.println("Balance: ₹" + acc10.getBalance());

            System.out.println("Attempting to withdraw ₹100.0 with incorrect PIN (9999)");
            try {
                acc10.withdraw(100.0, 9999);
            } catch (InvalidPinException e) {
                System.out.println("EXCEPTION: Incorrect PIN");
            }

            Account accNoPin = new Account(2000, "No Pin User", 25, 1000.0, "Savings");
            System.out.println("Attempting to withdraw ₹100.0 without PIN set");
            try {
                accNoPin.withdraw(100.0, 1234);
            } catch (InvalidPinException e) {
                System.out.println("EXCEPTION: PIN not set for this account");
            }

            validAccounts.add(acc10);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        System.out.println(">>> Test 11: All Accounts Summary");
        for (Account acc : validAccounts) {
            System.out.println(formatAccount(acc));
        }

        System.out.println("============================================================");
        System.out.println("TEST COMPLETED!");
        System.out.println("============================================================");
    }

    private static String formatAccount(Account acc) {
        String pinStatus = acc.hasPin() ? "Yes" : "No";
        return String.format("Account #%d | %s (%d yrs) | %s | ₹%.1f | %s | PIN: %s",
                acc.getAccountNumber(),
                acc.getName(),
                acc.getAge(),
                acc.getAccountType(),
                acc.getBalance(),
                acc.getStatus(),
                pinStatus
        );
    }
}