package com.gdb.domain;

public class AccountFactory {

    public static IAccount createAccount(String type, int accountNumber, String name, int age, double balance, int tenureYears) {
        if (type == null) {
            throw new IllegalArgumentException("Account type cannot be null.");
        }

        switch (type.toUpperCase().trim()) {
            case "SAVINGS":
                return new SavingsAccount(accountNumber, name, age, balance, tenureYears);
            case "CURRENT":
                return new CurrentAccount(accountNumber, name, age, balance, tenureYears);
            case "FIXED_DEPOSIT":
            case "FD":
                return new FixedDepositAccount(accountNumber, name, age, balance, tenureYears);
            case "SALARY":
                return new SalaryAccount(accountNumber, name, age, balance, tenureYears);
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }

    public static IAccount createAccount(String type, int accountNumber, String name, int age, double balance) {
        return createAccount(type, accountNumber, name, age, balance, 0);
    }

    public static IAccount createAccount(String type, int accountNumber, String name, double balance) {
        return createAccount(type, accountNumber, name, 25, balance, 0);
    }

    public static IAccount createAccount(String type, String name, double balance) {
        return createAccount(type, 1000 + (int)(Math.random() * 9000), name, 25, balance, 0);
    }

    public static IAccount createAccount(String type, double balance) {
        return createAccount(type, balance, 0);
    }

    public static IAccount createAccount(String type, double balance, int tenureYears) {
        if (type == null) {
            throw new IllegalArgumentException("Account type cannot be null.");
        }

        switch (type.toUpperCase().trim()) {
            case "SAVINGS":
                return new SavingsAccount(balance, tenureYears);
            case "CURRENT": {
                CurrentAccount ca = new CurrentAccount(balance);
                ca.setTenureYears(tenureYears);
                return ca;
            }
            case "FIXED_DEPOSIT":
            case "FD": {
                FixedDepositAccount fda = new FixedDepositAccount(balance);
                fda.setTenureYears(tenureYears);
                return fda;
            }
            case "SALARY": {
                SalaryAccount sa = new SalaryAccount(balance);
                sa.setTenureYears(tenureYears);
                return sa;
            }
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }
}
