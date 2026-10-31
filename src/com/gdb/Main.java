package com.gdb;

import com.gdb.domain.*;
import com.gdb.logging.*;
import com.gdb.service.AccountService;
import com.gdb.ui.AccountUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("    GLOBAL DIGITAL BANK — CONSOLE APPLICATION  ");
        System.out.println("============================================================\n");

        // Load rules
        AccountRulesEngine.getInstance();
        System.out.println(" Account rules loaded successfully!\n");

        // STEP 20 — Set up service with FileLogDestination
        FileLogDestination fileDest = new FileLogDestination();
        fileDest.clear(); // Start fresh for demo
        TransactionLogger logger = new TransactionLogger(fileDest);
        AccountService service = new AccountService(logger);

        // STEP 21 — Launch AccountUI
        AccountUI ui = new AccountUI(service);
        ui.start();
    }
}
