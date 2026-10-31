package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.logging.*;
import com.gdb.service.AccountService;
import com.gdb.ui.AccountUI;

import java.util.Scanner;

/**
 * Activity 20 — Test driver validating AccountUI integration and service endpoints.
 */
public class TestAccountUI {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 20 \u2014 ACCOUNT UI TEST");
        System.out.println("============================================================\n");

        try {
            // Wire dependencies
            MemoryLogDestination memDest = new MemoryLogDestination();
            TransactionLogger logger = new TransactionLogger(memDest);
            AccountService service = new AccountService(logger);

            // Simulate user inputs: open account, deposit, view, exit
            String simulatedInput = String.join("\n",
                "1",                    // Open Account
                "SAVINGS",              // Account type
                "Test User",            // Name
                "25",                   // Age
                "15000",                // Initial deposit
                "1234",                 // PIN
                "2",                    // Deposit
                "1001",                 // Account number
                "5000",                 // Amount
                "5",                    // View Account
                "1001",                 // Account number
                "7",                    // View Transaction History
                "9"                     // Exit
            ) + "\n";

            Scanner testScanner = new Scanner(simulatedInput);
            AccountUI ui = new AccountUI(service, testScanner);

            System.out.println("[TEST] Running simulated UI session...\n");
            ui.start();

            System.out.println("\n[TEST] UI integration test completed successfully!");

            // Verify service state
            System.out.println("\n[VERIFY] Account count: " + service.getAllAccounts().size() + " [EXPECTED: 1]");
            IAccount acc = service.getAccount(1001);
            System.out.println("[VERIFY] Account balance: Rs. " + acc.getBalance() + " [EXPECTED: 20000.0]");
            System.out.println("[VERIFY] Transaction history: " + service.getTransactionHistory().size() + " records [EXPECTED: 1]");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
