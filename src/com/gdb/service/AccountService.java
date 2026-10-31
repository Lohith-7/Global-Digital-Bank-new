package com.gdb.service;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.domain.AbstractAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.exceptions.AccountException;
import com.gdb.logging.TransactionLogger;
import com.gdb.repository.AccountRepository;
import com.gdb.repository.RepositoryFactory;
import com.gdb.repository.TransactionRepository;

import java.util.Collection;
import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionLogger logger;
    private final TransferService transferService;

    public AccountService(TransactionLogger logger) {
        this(logger, RepositoryFactory.getAccountRepository(), RepositoryFactory.getTransactionRepository());
    }

    public AccountService(TransactionLogger logger, AccountRepository accountRepository,
                         TransactionRepository transactionRepository) {
        this.logger = logger;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transferService = new TransferService();
    }

    public IAccount openAccount(String type, String name, int age, double initialBalance) {
        int accountNumber = accountRepository.nextAccountNumber();
        IAccount account = AccountFactory.createAccount(type, accountNumber, name, age, initialBalance, 0);
        accountRepository.save(account);
        return account;
    }

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
            accountRepository.update(account);
        }
    }

    public Transaction deposit(int accountNumber, double amount) throws Exception {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }

        AbstractAccount absAccount = (AbstractAccount) account;
        DepositCommand cmd = new DepositCommand(absAccount, amount);
        cmd.execute();
        logger.log(cmd);

        Transaction transaction = cmd.getTransaction();
        if (transaction != null) {
            transactionRepository.save(transaction);
        }
        return transaction;
    }

    public Transaction withdraw(int accountNumber, double amount, int pin) throws Exception {
        IAccount account = getAccount(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }

        AbstractAccount absAccount = (AbstractAccount) account;
        WithdrawCommand cmd = new WithdrawCommand(absAccount, amount, pin);
        cmd.execute();
        logger.log(cmd);

        Transaction transaction = cmd.getTransaction();
        if (transaction != null) {
            transactionRepository.save(transaction);
        }
        return transaction;
    }

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

        Transaction transaction = cmd.getTransaction();
        if (transaction != null) {
            transactionRepository.save(transaction);
        }
        return transaction;
    }

    public IAccount getAccount(int accountNumber) {
        return accountRepository.findById(accountNumber);
    }

    public Collection<IAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<TransactionCommand> getTransactionHistory() {
        return logger.readAll();
    }

    public List<Transaction> getRepositoryTransactionHistory() {
        return transactionRepository.findAll();
    }

    public int getNextAccountNumber() {
        return accountRepository.nextAccountNumber();
    }
}
