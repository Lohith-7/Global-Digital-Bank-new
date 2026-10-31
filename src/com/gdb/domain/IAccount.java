package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidAmountException;
import com.gdb.exceptions.InactiveAccountException;

public interface IAccount {
    int getAccountNumber();
    String getName();
    String getCustomerName();
    double getBalance();
    String getAccountType();
    String getStatus();

    void deposit(double amount) throws InvalidAmountException, InactiveAccountException, AccountException;
    void withdraw(double amount, String pin) throws AccountException;
    void withdraw(double amount, int pin) throws AccountException;
    void withdraw(double amount) throws AccountException;
    void displayAccountInfo();

    boolean canWithdraw(double amount);
    boolean verifyPin(String pin);
    boolean verifyPin(int pin);
    int getTenureYears();
    void setTenureYears(int tenureYears);
}
