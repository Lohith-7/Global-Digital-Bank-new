package com.gdb.domain;

import com.gdb.exceptions.*;

public class FixedDepositAccount extends AbstractAccount {
    private int tenureMonths = 12;
    private double interestRate = 6.5;

    public FixedDepositAccount(double balance) {
        super("FIXED_DEPOSIT", balance);
    }

    public FixedDepositAccount(double balance, int tenureMonths, double interestRate) {
        super("FIXED_DEPOSIT", balance);
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
    }

    public FixedDepositAccount(int accountNumber, String name, double balance) {
        super(accountNumber, name, balance, "FIXED_DEPOSIT");
    }

    public FixedDepositAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "FIXED_DEPOSIT");
    }

    public FixedDepositAccount(int accountNumber, String name, int age, double balance, int tenureYears) {
        super(accountNumber, name, age, balance, "FIXED_DEPOSIT");
        this.tenureYears = tenureYears;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {
        throw new AccountException("Premature withdrawals are not permitted on Fixed Deposit accounts before maturity.");
    }

    @Override
    public boolean canWithdraw(double amount) {
        return false;
    }

    public double calculateMaturityAmount() {
        double timeInYears = tenureMonths / 12.0;
        return getBalance() * Math.pow(1.0 + (interestRate / 100.0), timeInYears);
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}