package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.logging.TransactionLog;

import java.util.List;

public class TestCommandLogging {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 17 \u2014 COMMAND PATTERN + FILE LOGGING");
        System.out.println("============================================================\n");

        try {
            // 📝 STEP 10 — Create accounts and transaction log
            AbstractAccount acc1 = (AbstractAccount) AccountFactory.createAccount("SAVINGS", 1001, "John Doe", 25, 15000.0, 0);
            acc1.setPin(1234);
            AbstractAccount acc2 = (AbstractAccount) AccountFactory.createAccount("SAVINGS", 1002, "Jane Smith", 30, 10000.0, 0);
            acc2.setPin(1234);

            TransactionLog log = new TransactionLog();
            log.clear(); // Start fresh

            // 📝 STEP 11 — Execute and log DepositCommand
            DepositCommand depositCmd = new DepositCommand(acc1, 5000);
            depositCmd.execute();
            log.log(depositCmd);
            System.out.println("[STEP 11] Logged: " + depositCmd.getTransaction());

            // 📝 STEP 12 — Execute and log WithdrawCommand
            WithdrawCommand withdrawCmd = new WithdrawCommand(acc1, 2000, 1234);
            withdrawCmd.execute();
            log.log(withdrawCmd);
            System.out.println("[STEP 12] Logged: " + withdrawCmd.getTransaction());

            // 📝 STEP 13 — Execute and log TransferCommand
            TransferCommand transferCmd = new TransferCommand(acc1, acc2, 3000, 1234);
            transferCmd.execute();
            log.log(transferCmd);
            System.out.println("[STEP 13] Logged: " + transferCmd.getTransaction());

            // 📝 STEP 14 — Read history back and verify count
            List<TransactionCommand> history = log.readAll();
            System.out.println("\n[STEP 14] Read " + history.size() + " commands from transaction log:");
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
            }

            // 📝 STEP 15 — Verify file persistence with a new TransactionLog instance
            TransactionLog freshLog = new TransactionLog();
            List<TransactionCommand> freshHistory = freshLog.readAll();
            System.out.println("\n[STEP 15] Fresh reader verified " + freshHistory.size()
                    + " persisted commands [" + (freshHistory.size() == 3 ? "PASS" : "FAIL") + "]");

            // Clean up
            log.clear();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
