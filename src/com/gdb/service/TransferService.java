package com.gdb.service;

import com.gdb.domain.*;
import com.gdb.exceptions.*;
import java.time.LocalDateTime;

public class TransferService {

    // 📝 STEP 1 — service/TransferService.java -> transfer(from, to, amount, pin)
    public static void transfer(IAccount from, IAccount to, double amount, String pin) throws AccountException {
        // 1. Null check
        if (from == null || to == null) {
            throw new AccountException("Source and destination accounts are required");
        }

        // 2. Both accounts active
        if (!"Active".equalsIgnoreCase(from.getStatus()) || !"Active".equalsIgnoreCase(to.getStatus())) {
            throw new InactiveAccountException("Both accounts must be active to transfer funds");
        }

        // 3. from.verifyPin(pin)
        if (!from.verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }

        // 4. from.canWithdraw(amount)
        if (!from.canWithdraw(amount)) {
            throw new InsufficientBalanceException("Insufficient balance for transfer of Rs. " + amount);
        }

        // 5. Cast from to AbstractAccount / Account, call resetDailyTransferIfNeeded(), then canTransfer(amount)
        AbstractAccount source;
        if (from instanceof AbstractAccount) {
            source = (AbstractAccount) from;
        } else {
            throw new AccountException("Unsupported account implementation");
        }

        source.resetDailyTransferIfNeeded();
        if (!source.canTransfer(amount)) {
            double remaining = source.getRemainingDailyTransferLimit();
            throw new AccountException("Daily transfer limit exceeded. Remaining today: Rs. " + remaining);
        }

        // 6. Debit
        from.withdraw(amount, pin);

        // 7. Credit
        to.deposit(amount);

        // 8. Record
        source.updateDailyTransferTotal(amount);
    }

    public static void transfer(IAccount from, IAccount to, double amount, int pin) throws AccountException {
        transfer(from, to, amount, String.valueOf(pin));
    }

    // 📝 STEP 9 — transferWithTransaction(from, to, amount, pin)
    public static Transaction transferWithTransaction(IAccount from, IAccount to, double amount, int pin) throws AccountException {
        transfer(from, to, amount, pin);
        return new Transaction(
            Transaction.generateId(),
            LocalDateTime.now(),
            from.getAccountNumber(),
            TransactionType.TRANSFER,
            amount,
            from.getBalance(),
            "SUCCESS",
            "Transfer of Rs. " + amount + " to Account #" + to.getAccountNumber(),
            from.getAccountNumber(),
            to.getAccountNumber()
        );
    }

    public static Transaction transferWithTransaction(IAccount from, IAccount to, double amount, String pin) throws AccountException {
        return transferWithTransaction(from, to, amount, Integer.parseInt(pin));
    }
}

