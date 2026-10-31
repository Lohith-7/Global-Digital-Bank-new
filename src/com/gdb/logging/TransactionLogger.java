package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.List;

// 📝 STEPS 17–23 — TransactionLogger (Bridge Abstraction)
public class TransactionLogger {
    // STEP 17 — Declare field
    protected LogDestination destination;

    // STEP 18 — Constructor
    public TransactionLogger(LogDestination destination) {
        this.destination = destination;
    }

    // STEP 19 — setDestination (runtime switching)
    public void setDestination(LogDestination destination) {
        this.destination = destination;
    }

    // STEP 20 — log
    public void log(TransactionCommand cmd) {
        destination.write(cmd);
    }

    // STEP 21 — readAll
    public List<TransactionCommand> readAll() {
        return destination.readAll();
    }

    // STEP 22 — clear
    public void clear() {
        destination.clear();
    }

    // STEP 23 — getDestinationName
    public String getDestinationName() {
        return destination.getDestinationName();
    }
}
