package com.gdb.command;

import com.gdb.domain.AbstractAccount;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

// 📝 STEP 5 — TransferCommand
public class TransferCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;

    // Fields
    private final IAccount fromAccount;
    private final IAccount toAccount;
    private final double amount;
    private final int pin;
    private Transaction transaction;

    // Constructor
    public TransferCommand(IAccount fromAccount, IAccount toAccount, double amount, int pin) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.pin = pin;
    }

    // execute()
    @Override
    public void execute() throws Exception {
        this.transaction = TransferService.transferWithTransaction(fromAccount, toAccount, amount, pin);
    }

    // getTransaction()
    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
