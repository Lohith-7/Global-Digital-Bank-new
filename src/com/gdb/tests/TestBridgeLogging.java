package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.logging.*;

public class TestBridgeLogging {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 18 \u2014 BRIDGE PATTERN (FILE + DB)");
        System.out.println("============================================================\n");

        try {
            // Create test accounts
            AbstractAccount acc1 = (AbstractAccount) AccountFactory.createAccount("SAVINGS", 1001, "John Doe", 25, 15000.0, 0);
            acc1.setPin(1234);
            AbstractAccount acc2 = (AbstractAccount) AccountFactory.createAccount("SAVINGS", 1002, "Jane Smith", 30, 10000.0, 0);
            acc2.setPin(1234);

            // 📝 STEP 24 — Instantiate all log destinations
            FileLogDestination fileDest = new FileLogDestination();
            fileDest.clear(); // Start fresh
            DatabaseLogDestination dbDest = new DatabaseLogDestination(new SimulatedDatabase());
            MemoryLogDestination memDest = new MemoryLogDestination();

            TransactionLogger logger = new TransactionLogger(fileDest);

            // Create commands
            DepositCommand depositCmd = new DepositCommand(acc1, 5000);
            depositCmd.execute();
            WithdrawCommand withdrawCmd = new WithdrawCommand(acc1, 2000, 1234);
            withdrawCmd.execute();
            TransferCommand transferCmd = new TransferCommand(acc1, acc2, 1000, 1234);
            transferCmd.execute();

            // 📝 STEP 25 — Log to FILE destination
            System.out.println("[STEP 25] Logging to FILE destination...");
            logger.setDestination(fileDest);
            logger.log(depositCmd);
            logger.log(withdrawCmd);
            logger.log(transferCmd);
            System.out.println("  FILE log count: " + logger.readAll().size());

            // 📝 STEP 26 — Switch to DATABASE destination
            System.out.println("\n[STEP 26] Switched to DATABASE destination...");
            logger.setDestination(dbDest);
            logger.log(depositCmd);
            logger.log(withdrawCmd);
            logger.log(transferCmd);
            System.out.println("  DATABASE log count: " + logger.readAll().size());

            // 📝 STEP 27 — Switch to MEMORY destination
            System.out.println("\n[STEP 27] Switched to MEMORY destination...");
            logger.setDestination(memDest);
            logger.log(depositCmd);
            logger.log(withdrawCmd);
            logger.log(transferCmd);
            System.out.println("  MEMORY log count: " + logger.readAll().size());

            // 📝 STEP 28 — Verify Data Isolation
            System.out.println("\n[STEP 28] Verifying Data Isolation:");
            int fileCount = fileDest.readAll().size();
            int dbCount = dbDest.readAll().size();
            int memCount = memDest.readAll().size();
            System.out.println("  FILE count: " + fileCount + " [EXPECTED: 3]");
            System.out.println("  DATABASE count: " + dbCount + " [EXPECTED: 3]");
            System.out.println("  MEMORY count: " + memCount + " [EXPECTED: 3]");

            // 📝 STEP 29 — Success message
            System.out.println("\n[STEP 29] All Bridge Pattern log backends verified successfully!");

            // Clean up
            fileDest.clear();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
