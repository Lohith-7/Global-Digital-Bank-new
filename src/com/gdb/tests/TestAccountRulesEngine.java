package com.gdb.tests;

import com.gdb.domain.*;

public class TestAccountRulesEngine {
    public static void main(String[] args) {
        testActivity13_1();
        System.out.println();
        testActivity13_2();
    }

    private static void testActivity13_1() {
        System.out.println("=== Activity 13.1: Hardcoded Rules Engine Test ===");
        System.out.println(String.format("Tenure 0 yrs -> Min Balance: Rs %.1f | Interest: %.1f%%",
                AccountRulesEngine.getSavingsMinBalance(0), AccountRulesEngine.getSavingsInterestRate(0)));
        System.out.println(String.format("Tenure 2 yrs -> Min Balance: Rs %.1f  | Interest: %.1f%%",
                AccountRulesEngine.getSavingsMinBalance(2), AccountRulesEngine.getSavingsInterestRate(2)));
        System.out.println(String.format("Tenure 4 yrs -> Min Balance: Rs %.1f  | Interest: %.1f%%",
                AccountRulesEngine.getSavingsMinBalance(4), AccountRulesEngine.getSavingsInterestRate(4)));
        System.out.println(String.format("Tenure 6 yrs -> Min Balance: Rs %.1f  | Interest: %.1f%%",
                AccountRulesEngine.getSavingsMinBalance(6), AccountRulesEngine.getSavingsInterestRate(6)));
        System.out.println("Rules Engine lookup completed successfully!");
    }

    private static void testActivity13_2() {
        System.out.println("=== Activity 13.2: Dynamic Account Rules Test ===");
        SavingsAccount sa = (SavingsAccount) AccountFactory.createAccount("SAVINGS", 1001, "Test User", 30, 20000.0, 4);
        System.out.println(String.format("Created Savings Account (Tenure: %d yrs):", sa.getTenureYears()));
        System.out.println(String.format(" -> Min Balance: Rs %.1f (Dynamically fetched)", sa.getMinimumBalance()));
        System.out.println(String.format(" -> Interest Rate: %.1f%% (Dynamically fetched)", sa.getInterestRate()));
        System.out.println("Dynamic rule integration verified!");
    }
}
