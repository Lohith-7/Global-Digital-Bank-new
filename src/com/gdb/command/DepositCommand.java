package com.gdb.command;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.Transaction;

// 📝 STEP 3 — DepositCommand
public class DepositCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;

    // Fields
    private final AbstractAccount account;
    private final double amount;
    private Transaction transaction;

    // Constructor
    public DepositCommand(AbstractAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    // execute()
    @Override
    public void execute() throws Exception {
        this.transaction = account.depositWithTransaction(amount);
    }

    // getTransaction()
    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
