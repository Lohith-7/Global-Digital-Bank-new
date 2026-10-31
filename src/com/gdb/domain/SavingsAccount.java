package com.gdb.domain;

import com.gdb.exceptions.*;

public class SavingsAccount extends AbstractAccount {
    private double minBalance = 1000.0;
    private double interestRate = 4.0;

    public SavingsAccount(double balance) {
        super("SAVINGS", balance);
        this.minBalance = 1000.0;
        this.interestRate = 4.0;
    }

    public SavingsAccount(double balance, int tenureYears) {
        super("SAVINGS", balance);
        this.tenureYears = tenureYears;
        applyDynamicRules();
    }

    public SavingsAccount(double balance, double minBalance, double interestRate) {
        super("SAVINGS", balance);
        this.minBalance = minBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(int accountNumber, String name, double balance) {
        super(accountNumber, name, balance, "SAVINGS");
        this.minBalance = 1000.0;
        this.interestRate = 4.0;
    }

    public SavingsAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "SAVINGS");
        this.minBalance = 1000.0;
        this.interestRate = 4.0;
    }

    public SavingsAccount(int accountNumber, String name, int age, double balance, int tenureYears) {
        super(accountNumber, name, age, balance, "SAVINGS");
        this.tenureYears = tenureYears;
        applyDynamicRules();
    }

    private void applyDynamicRules() {
        this.minBalance = AccountRulesEngine.getSavingsMinBalance(this.tenureYears);
        this.interestRate = AccountRulesEngine.getSavingsInterestRate(this.tenureYears);
    }

    @Override
    public void setTenureYears(int tenureYears) {
        super.setTenureYears(tenureYears);
        applyDynamicRules();
    }

    @Override
    protected void processDebit(double amount) throws MinimumBalanceViolationException, InsufficientBalanceException {
        if ((this.balance - amount) < this.minBalance) {
            throw new MinimumBalanceViolationException(
                "Cannot withdraw: balance would drop below minimum balance of " + minBalance
            );
        }
        this.balance -= amount;
    }

    @Override
    public boolean canWithdraw(double amount) {
        if (amount <= 0) return false;
        return (this.balance - amount) >= this.minBalance;
    }

    public void applyInterest() {
        double interest = getBalance() * (interestRate / 100.0);
        try {
            deposit(interest);
        } catch (InvalidAmountException | InactiveAccountException e) {
            System.err.println(e.getMessage());
        }
    }

    public void applyMonthlyInterest() {
        applyInterest();
    }

    public double getMinimumBalance() {
        return minBalance;
    }

    public void setMinimumBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}