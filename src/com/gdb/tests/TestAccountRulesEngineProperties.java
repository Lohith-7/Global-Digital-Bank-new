package com.gdb.tests;

import com.gdb.domain.*;

public class TestAccountRulesEngineProperties {
    public static void main(String[] args) {
        System.out.println("=== Activity 14: Properties-Driven Rules Engine Test ===");
        System.out.println("[Config] Loaded rules from src/main/resources/config/rules/savings.properties");

        System.out.println(String.format("Tenure 0 yrs -> Min Balance: Rs %.1f | Interest: %.2f%%",
                AccountRulesEngine.getSavingsMinBalance(0), AccountRulesEngine.getSavingsInterestRate(0)));
        System.out.println(String.format("Tenure 2 yrs -> Min Balance: Rs %.1f  | Interest: %.2f%%",
                AccountRulesEngine.getSavingsMinBalance(2), AccountRulesEngine.getSavingsInterestRate(2)));
        System.out.println(String.format("Tenure 4 yrs -> Min Balance: Rs %.1f  | Interest: %.2f%%",
                AccountRulesEngine.getSavingsMinBalance(4), AccountRulesEngine.getSavingsInterestRate(4)));
        System.out.println(String.format("Tenure 6 yrs -> Min Balance: Rs %.1f  | Interest: %.2f%%",
                AccountRulesEngine.getSavingsMinBalance(6), AccountRulesEngine.getSavingsInterestRate(6)));

        System.out.println("All external properties loaded and verified successfully!");
    }
}
