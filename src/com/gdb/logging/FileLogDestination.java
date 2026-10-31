package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.List;

// 📝 STEPS 5–10 — FileLogDestination
public class FileLogDestination implements LogDestination {
    // STEP 5 — Declare field
    private TransactionLog log;

    // STEP 6 — Constructor
    public FileLogDestination() {
        this.log = new TransactionLog();
    }

    // STEP 7 — write
    @Override
    public void write(TransactionCommand cmd) {
        log.log(cmd);
    }

    // STEP 8 — readAll
    @Override
    public List<TransactionCommand> readAll() {
        return log.readAll();
    }

    // STEP 9 — clear
    @Override
    public void clear() {
        log.clear();
    }

    // STEP 10 — getDestinationName
    @Override
    public String getDestinationName() {
        return "FILE";
    }
}
