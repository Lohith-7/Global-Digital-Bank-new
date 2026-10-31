package com.gdb.domain;

import com.gdb.exceptions.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class AbstractAccount implements IAccount, Serializable {
    private static final long serialVersionUID = 1L;

    protected static final int MIN_AGE = 18;
    protected static final int MIN_PIN = 1000;
    protected static final int MAX_PIN = 9999;

    protected int accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected String pin;

    // STEP 2.1 — daily tracking fields and tenure
    protected int tenureYears = 0;
    protected double dailyTransferTotal = 0.0;
    protected LocalDateTime lastTransferDate = LocalDateTime.now();

    public AbstractAccount() {
        this.status = "Active";
        this.pin = "1234";
        this.lastTransferDate = LocalDateTime.now();
    }

    public AbstractAccount(String accountType, double balance) {
        this.accountType = accountType;
        this.balance = balance;
        this.status = "Active";
        this.pin = "1234";
        this.lastTransferDate = LocalDateTime.now();
    }

    public AbstractAccount(int accountNumber, String name, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = "1234";
        this.lastTransferDate = LocalDateTime.now();
    }

    public AbstractAccount(int accountNumber, String name, int age, double balance, String accountType) throws IllegalArgumentException {
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("Age must be 18 or older to open an account.");
        }
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = "1234";
        this.lastTransferDate = LocalDateTime.now();
    }

    // Concrete Template Method for Withdrawal
    @Override
    public void withdraw(double amount, String pin) throws AccountException {
        // Step 1: Validate PIN
        if (!validatePin(pin)) {
            throw new InvalidPinException("Incorrect PIN or PIN is not set.");
        }

        // Step 2: Validate account status
        checkActive();

        // Step 3: Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException("Withdraw amount must be greater than 0.");
        }

        // Step 4: Execute subclass-specific debit logic
        processDebit(amount);
    }

    @Override
    public void withdraw(double amount, int pin) throws AccountException {
        withdraw(amount, String.valueOf(pin));
    }

    @Override
    public void withdraw(double amount) throws AccountException {
        withdraw(amount, this.pin != null ? this.pin : "1234");
    }

    // Primitive hook for Template Method Pattern
    protected abstract void processDebit(double amount) throws AccountException;

    // canWithdraw contract
    @Override
    public abstract boolean canWithdraw(double amount);

    // Shared concrete deposit method
    @Override
    public void deposit(double amount) throws InvalidAmountException, InactiveAccountException {
        checkActive();
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive: " + amount);
        }
        this.balance += amount;
    }

    // Shared concrete PIN validation
    public boolean validatePin(String pin) {
        return this.pin != null && this.pin.equals(pin);
    }

    public boolean validatePin(int pin) {
        return validatePin(String.valueOf(pin));
    }

    @Override
    public boolean verifyPin(int pin) {
        return validatePin(pin);
    }

    @Override
    public boolean verifyPin(String pin) {
        return validatePin(pin);
    }

    public boolean hasPin() {
        return this.pin != null;
    }

    public void setPin(int pin) throws IllegalArgumentException {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException("PIN must be a 4-digit number (1000 to 9999).");
        }
        this.pin = String.valueOf(pin);
    }

    public void setPin(String pin) throws IllegalArgumentException {
        if (pin == null || pin.length() != 4 || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be a 4-digit number (1000 to 9999).");
        }
        this.pin = pin;
    }

    public void changePin(String oldPin, String newPin) throws InvalidPinException, IllegalArgumentException {
        if (!validatePin(oldPin)) {
            throw new InvalidPinException("Incorrect old PIN.");
        }
        if (newPin == null || newPin.length() != 4 || !newPin.matches("\\d{4}")) {
            throw new IllegalArgumentException("New PIN must be a 4-digit number (1000 to 9999).");
        }
        this.pin = newPin;
    }

    public void changePin(int oldPin, int newPin) throws InvalidPinException, IllegalArgumentException {
        changePin(String.valueOf(oldPin), String.valueOf(newPin));
    }

    @Override
    public void displayAccountInfo() {
        System.out.println(String.format(
            "Account #%d | %s (%d yrs, Tenure: %d yrs) | %s | Rs. %.1f | %s",
            accountNumber, (name != null ? name : "N/A"), age, tenureYears, accountType, balance, status
        ));
    }

    public void checkActive() throws InactiveAccountException {
        if (!"Active".equalsIgnoreCase(this.status)) {
            throw new InactiveAccountException("Cannot perform operation on an inactive account.");
        }
    }

    public void closeAccount() throws IllegalStateException {
        if ("Inactive".equalsIgnoreCase(this.status)) {
            throw new IllegalStateException("Account is already closed.");
        }
        this.status = "Inactive";
    }

    public void reopenAccount() throws IllegalStateException {
        if ("Active".equalsIgnoreCase(this.status)) {
            throw new IllegalStateException("Account is already active.");
        }
        this.status = "Active";
    }

    // STEP 3 — Account.getDailyTransferLimit()
    public double getDailyTransferLimit() {
        return AccountRulesEngine.getInstance().getDailyTransferLimit(getAccountType(), getTenureYears());
    }

    // STEP 4 — Account.getRemainingDailyTransferLimit()
    public double getRemainingDailyTransferLimit() {
        resetDailyTransferIfNeeded();
        return Math.max(0.0, getDailyTransferLimit() - dailyTransferTotal);
    }

    // STEP 5 — Account.canTransfer(double amount)
    public boolean canTransfer(double amount) {
        resetDailyTransferIfNeeded();
        return (dailyTransferTotal + amount) <= getDailyTransferLimit();
    }

    // STEP 6 — Account.updateDailyTransferTotal(double amount)
    public void updateDailyTransferTotal(double amount) {
        resetDailyTransferIfNeeded();
        this.dailyTransferTotal += amount;
        this.lastTransferDate = LocalDateTime.now();
    }

    // STEP 7 — Account.resetDailyTransferIfNeeded()
    public void resetDailyTransferIfNeeded() {
        if (!this.lastTransferDate.toLocalDate().equals(LocalDate.now())) {
            this.dailyTransferTotal = 0.0;
            this.lastTransferDate = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public double getDailyTransferTotal() {
        return dailyTransferTotal;
    }

    public LocalDateTime getLastTransferDate() {
        return lastTransferDate;
    }

    @Override
    public int getTenureYears() {
        return tenureYears;
    }

    @Override
    public void setTenureYears(int tenureYears) {
        this.tenureYears = tenureYears;
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCustomerName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    // 📝 STEP 7 — depositWithTransaction(double amount)
    public Transaction depositWithTransaction(double amount) throws AccountException {
        deposit(amount);
        return new Transaction(
            Transaction.generateId(),
            java.time.LocalDateTime.now(),
            this.accountNumber,
            TransactionType.DEPOSIT,
            amount,
            this.balance,
            "SUCCESS",
            "Deposit of Rs. " + amount,
            0,
            0
        );
    }

    // 📝 STEP 8 — withdrawWithTransaction(double amount, int pin)
    public Transaction withdrawWithTransaction(double amount, int pin) throws AccountException {
        withdraw(amount, pin);
        return new Transaction(
            Transaction.generateId(),
            java.time.LocalDateTime.now(),
            this.accountNumber,
            TransactionType.WITHDRAW,
            amount,
            this.balance,
            "SUCCESS",
            "Withdrawal of Rs. " + amount,
            0,
            0
        );
    }

    @Override
    public String toString() {
        return String.format("Account #%d | %s (%d yrs, Tenure: %d yrs) | %s | Rs. %s | %s",
                accountNumber, (name != null ? name : "N/A"), age, tenureYears, accountType, balance, status);
    }
}
