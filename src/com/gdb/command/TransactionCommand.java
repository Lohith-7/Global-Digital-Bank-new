package com.gdb.command;

import com.gdb.domain.Transaction;
import java.io.Serializable;

public interface TransactionCommand extends Serializable {
    // 📝 STEP 1 — Declare execute method
    void execute() throws Exception;

    // 📝 STEP 2 — Declare getTransaction method
    Transaction getTransaction();
}
