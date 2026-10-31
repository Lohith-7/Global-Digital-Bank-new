package com.gdb.domain;

import com.gdb.exceptions.*;

public class Account extends AbstractAccount {
    private static final double MIN_BALANCE_SAVINGS = 500.0;
    private static final double MIN_BALANCE_CURRENT = 1000.0;

    public Account(int accNumber, String name, int age, double balance, String accType) throws IllegalArgumentException {
        super(accNumber, name, age, balance, accType);
        if (accType == null || (!accType.equalsIgnoreCase("Savings") && !accType.equalsIgnoreCase("Current"))) {
            throw new IllegalArgumentException("Invalid account type. Choose Savings or Current.");
        }

        this.accountType = accType.equalsIgnoreCase("Savings") ? "Savings" : "Current";

        double requiredBalance = this.accountType.equals("Savings") ? MIN_BALANCE_SAVINGS : MIN_BALANCE_CURRENT;
        if (balance < requiredBalance) {
            throw new IllegalArgumentException("Initial deposit too low. Minimum is " + requiredBalance);
        }

        this.status = "Active";
        this.pin = null;
    }

    public Account(String accType, double balance) {
        super(accType, balance);
        this.status = "Active";
        this.pin = "1234";
    }

    @Override
    protected void processDebit(double amount) throws AccountException {
        if (amount > this.balance) {
            throw new InsufficientBalanceException("Not enough balance for withdrawal.");
        }

        double minBal = getMinBalance();
        if ((this.balance - amount) < minBal) {
            throw new MinimumBalanceViolationException("Cannot withdraw: balance would drop below minimum " + minBal);
        }

        this.balance -= amount;
    }

    @Override
    public boolean canWithdraw(double amount) {
        if (amount <= 0) return false;
        double minBal = getMinBalance();
        return (this.balance - amount) >= minBal;
    }

    private double getMinBalance() {
        return "Savings".equalsIgnoreCase(this.accountType) ? MIN_BALANCE_SAVINGS : MIN_BALANCE_CURRENT;
    }
}