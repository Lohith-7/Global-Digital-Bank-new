package com.gdb.domain;

import com.gdb.exceptions.*;

public class CurrentAccount extends AbstractAccount {
    private double overdraftLimit = 25000.0;

    public CurrentAccount(double balance) {
        super("CURRENT", balance);
    }

    public CurrentAccount(double balance, double overdraftLimit) {
        super("CURRENT", balance);
        this.overdraftLimit = overdraftLimit;
    }

    public CurrentAccount(int accountNumber, String name, double balance) {
        super(accountNumber, name, balance, "CURRENT");
    }

    public CurrentAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "CURRENT");
    }

    public CurrentAccount(int accountNumber, String name, int age, double balance, int tenureYears) {
        super(accountNumber, name, age, balance, "CURRENT");
        this.tenureYears = tenureYears;
    }

    @Override
    protected void processDebit(double amount) throws InsufficientBalanceException {
        if (amount > (this.balance + this.overdraftLimit)) {
            throw new InsufficientBalanceException(
                "Requested amount exceeds balance and overdraft limit."
            );
        }
        this.balance -= amount;
    }

    @Override
    public boolean canWithdraw(double amount) {
        if (amount <= 0) return false;
        return amount <= (this.balance + this.overdraftLimit);
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}