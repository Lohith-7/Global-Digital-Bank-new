package com.gdb.ui;

import com.gdb.command.TransactionCommand;
import com.gdb.domain.*;
import com.gdb.service.AccountService;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Activity 20 — Interactive menu-driven console UI built on top of AccountService.
 */
public class AccountUI {
    private final AccountService service;
    private final Scanner scanner;

    public AccountUI(AccountService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public AccountUI(AccountService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        openAccount();
                        break;
                    case "2":
                        depositMoney();
                        break;
                    case "3":
                        withdrawMoney();
                        break;
                    case "4":
                        transferMoney();
                        break;
                    case "5":
                        viewAccount();
                        break;
                    case "6":
                        viewAllAccounts();
                        break;
                    case "7":
                        viewTransactionHistory();
                        break;
                    case "8":
                        closeAccount();
                        break;
                    case "9":
                        running = false;
                        System.out.println("\n\uD83D\uDC4B Thank you for using Global Digital Bank. Goodbye!");
                        break;
                    default:
                        System.out.println("\n\u274C Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("\n\u274C Error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n============================================================");
        System.out.println("          GLOBAL DIGITAL BANK  ");
        System.out.println("============================================================");
        System.out.println("  1. Open New Account");
        System.out.println("  2. Deposit Money");
        System.out.println("  3. Withdraw Money");
        System.out.println("  4. Transfer Money");
        System.out.println("  5. View Account Details");
        System.out.println("  6. View All Accounts");
        System.out.println("  7. View Transaction History");
        System.out.println("  8. Close Account");
        System.out.println("  9. Exit");
        System.out.println("============================================================");
        System.out.print("  Enter your choice: ");
    }

    private void openAccount() {
        System.out.print("  Account type (SAVINGS/CURRENT/SALARY): ");
        String type = scanner.nextLine().trim();
        System.out.print("  Customer name: ");
        String name = scanner.nextLine().trim();
        System.out.print("  Age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("  Initial deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("  Set 4-digit PIN: ");
        int pin = Integer.parseInt(scanner.nextLine().trim());

        IAccount account = service.openAccount(type, name, age, amount);
        ((AbstractAccount) account).setPin(pin);
        System.out.println("\n  \u2705 Account opened successfully!");
        System.out.println("  " + account);
    }

    private void depositMoney() throws Exception {
        System.out.print("  Account number: ");
        int accNo = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("  Amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        Transaction txn = service.deposit(accNo, amount);
        System.out.println("\n  \u2705 " + txn);
    }

    private void withdrawMoney() throws Exception {
        System.out.print("  Account number: ");
        int accNo = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("  Amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("  PIN: ");
        int pin = Integer.parseInt(scanner.nextLine().trim());

        Transaction txn = service.withdraw(accNo, amount, pin);
        System.out.println("\n  \u2705 " + txn);
    }

    private void transferMoney() throws Exception {
        System.out.print("  From account number: ");
        int fromAcc = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("  To account number: ");
        int toAcc = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("  Amount to transfer: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("  PIN: ");
        int pin = Integer.parseInt(scanner.nextLine().trim());

        Transaction txn = service.transfer(fromAcc, toAcc, amount, pin);
        System.out.println("\n  \u2705 " + txn);
    }

    private void viewAccount() {
        System.out.print("  Account number: ");
        int accNo = Integer.parseInt(scanner.nextLine().trim());

        IAccount account = service.getAccount(accNo);
        if (account == null) {
            System.out.println("\n  \u274C Account not found: " + accNo);
        } else {
            System.out.println("\n  " + account);
        }
    }

    private void viewAllAccounts() {
        Collection<IAccount> all = service.getAllAccounts();
        if (all.isEmpty()) {
            System.out.println("\n  No accounts found.");
        } else {
            System.out.println("\n  All Accounts (" + all.size() + "):");
            for (IAccount acc : all) {
                System.out.println("    " + acc);
            }
        }
    }

    private void viewTransactionHistory() {
        List<TransactionCommand> history = service.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("\n  No transactions recorded yet.");
        } else {
            System.out.println("\n  Transaction History (" + history.size() + " records):");
            for (int i = 0; i < history.size(); i++) {
                System.out.println("    [" + (i + 1) + "] " + history.get(i).getTransaction());
            }
        }
    }

    private void closeAccount() throws Exception {
        System.out.print("  Account number: ");
        int accNo = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("  PIN: ");
        int pin = Integer.parseInt(scanner.nextLine().trim());

        service.closeAccount(accNo, pin);
        System.out.println("\n  \u2705 Account #" + accNo + " closed successfully.");
    }
}
