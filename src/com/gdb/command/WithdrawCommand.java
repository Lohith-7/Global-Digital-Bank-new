package com.gdb.command;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.Transaction;

// 📝 STEP 4 — WithdrawCommand
public class WithdrawCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;

    // Fields
    private final AbstractAccount account;
    private final double amount;
    private final int pin;
    private Transaction transaction;

    // Constructor
    public WithdrawCommand(AbstractAccount account, double amount, int pin) {
        this.account = account;
        this.amount = amount;
        this.pin = pin;
    }

    // execute()
    @Override
    public void execute() throws Exception {
        this.transaction = account.withdrawWithTransaction(amount, pin);
    }

    // getTransaction()
    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
