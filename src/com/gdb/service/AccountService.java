package com.gdb.service;

import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.exceptions.AccountException;
import com.gdb.logging.TransactionLogger;

import java.util.*;

public class AccountService {
    // 📝 STEP 1 — Fields
    private Map<Integer, IAccount> accounts;
    private TransactionLogger logger;
    private TransferService transferService;
    private int nextAccountNumber = 1001;

    // 📝 STEP 2 — Constructor
    public AccountService(TransactionLogger logger) {
        this.accounts = new HashMap<>();
        this.logger = logger;
        this.transferService = new TransferService();
    }

    // 📝 STEP 3 — openAccount(type, name, age, initialBalance)
    public IAccount openAccount(String type, String name, int age, double initialBalance) {
        int accountNumber = nextAccountNumber++;
        IAccount account = AccountFactory.createAccount(type, accountNumber, name, age, initialBalance, 0);
        accounts.put(accountNumber, account);
        return account;
    }

    // 📝 STEP 4 — closeAccount(accountNumber, pin)
    public void closeAccount(int accountNumber, int pin) throws AccountException {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        if (!account.verifyPin(pin)) {
            throw new com.gdb.exceptions.InvalidPinException("Incorrect PIN");
        }
        if (account instanceof AbstractAccount) {
            ((AbstractAccount) account).closeAccount();
        }
    }

    // 📝 STEP 5 — deposit(accountNumber, amount)
    public Transaction deposit(int accountNumber, double amount) throws Exception {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        AbstractAccount absAccount = (AbstractAccount) account;
        DepositCommand cmd = new DepositCommand(absAccount, amount);
        cmd.execute();
        logger.log(cmd);
        return cmd.getTransaction();
    }

    // 📝 STEP 6 — withdraw(accountNumber, amount, pin)
    public Transaction withdraw(int accountNumber, double amount, int pin) throws Exception {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        AbstractAccount absAccount = (AbstractAccount) account;
        WithdrawCommand cmd = new WithdrawCommand(absAccount, amount, pin);
        cmd.execute();
        logger.log(cmd);
        return cmd.getTransaction();
    }

    // 📝 STEP 7 — transfer(fromAcc, toAcc, amount, pin)
    public Transaction transfer(int fromAcc, int toAcc, double amount, int pin) throws Exception {
        IAccount from = getAccount(fromAcc);
        IAccount to = getAccount(toAcc);
        if (from == null) {
            throw new AccountException("Account not found: " + fromAcc);
        }
        if (to == null) {
            throw new AccountException("Account not found: " + toAcc);
        }
        TransferCommand cmd = new TransferCommand(from, to, amount, pin);
        cmd.execute();
        logger.log(cmd);
        return cmd.getTransaction();
    }

    // 📝 STEP 8 — getAccount(accNo)
    public IAccount getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    // 📝 STEP 9 — getAllAccounts()
    public Collection<IAccount> getAllAccounts() {
        return accounts.values();
    }

    // 📝 STEP 10 — getTransactionHistory()
    public List<TransactionCommand> getTransactionHistory() {
        return logger.readAll();
    }

    // 📝 STEP 11 — getNextAccountNumber()
    public int getNextAccountNumber() {
        return nextAccountNumber;
    }
}
