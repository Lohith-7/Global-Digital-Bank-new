package com.gdb.domain;

import com.gdb.exceptions.*;

public class SalaryAccount extends AbstractAccount {
    private String employerName;
    private int inactiveMonths;

    public SalaryAccount(double balance, String employerName) {
        super("SALARY", balance);
        this.employerName = employerName;
        this.inactiveMonths = 0;
    }

    public SalaryAccount(double balance) {
        super("SALARY", balance);
        this.employerName = "Corporate";
        this.inactiveMonths = 0;
    }

    public SalaryAccount(int accountNumber, String name, double balance) {
        super(accountNumber, name, balance, "SALARY");
        this.employerName = "Corporate";
        this.inactiveMonths = 0;
    }

    public SalaryAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "SALARY");
        this.employerName = "Corporate";
        this.inactiveMonths = 0;
    }

    public SalaryAccount(int accountNumber, String name, int age, double balance, String employerName) {
        super(accountNumber, name, age, balance, "SALARY");
        this.employerName = employerName;
        this.inactiveMonths = 0;
    }

    public SalaryAccount(int accountNumber, String name, int age, double balance, int tenureYears) {
        super(accountNumber, name, age, balance, "SALARY");
        this.employerName = "Corporate";
        this.inactiveMonths = 0;
        this.tenureYears = tenureYears;
    }

    @Override
    protected void processDebit(double amount) throws InsufficientBalanceException {
        if (amount > this.balance) {
            throw new InsufficientBalanceException("Insufficient balance in salary account.");
        }
        this.balance -= amount;
    }

    @Override
    public boolean canWithdraw(double amount) {
        if (amount <= 0) return false;
        return amount <= this.balance;
    }

    public void checkSalaryCreditHistory() {
        System.out.println("Checking salary credit history for employer: " + (employerName != null ? employerName : "Corporate"));
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public int getInactiveMonths() {
        return inactiveMonths;
    }

    public void setInactiveMonths(int inactiveMonths) {
        this.inactiveMonths = inactiveMonths;
    }
}
