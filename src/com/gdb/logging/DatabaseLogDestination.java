package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.List;

// 📝 STEPS 11–16 — DatabaseLogDestination
public class DatabaseLogDestination implements LogDestination {
    // STEP 11 — Declare field
    private final SimulatedDatabase db;

    // STEP 12 — Constructor
    public DatabaseLogDestination(SimulatedDatabase db) {
        this.db = db;
    }

    // STEP 13 — write
    @Override
    public void write(TransactionCommand cmd) {
        db.insert("transaction_log", cmd);
    }

    // STEP 14 — readAll
    @Override
    public List<TransactionCommand> readAll() {
        return db.selectAll("transaction_log");
    }

    // STEP 15 — clear
    @Override
    public void clear() {
        db.deleteAll("transaction_log");
    }

    // STEP 16 — getDestinationName
    @Override
    public String getDestinationName() {
        return "DATABASE";
    }
}
