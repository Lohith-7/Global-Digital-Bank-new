package com.gdb.domain;

import java.util.*;

public class AccountRulesEngine {
    private static AccountRulesEngine instance;
    private final AccountRulesPropertiesLoader propertiesLoader;

    // In-memory tables for Activity 13.1
    private static final Map<String, Double> IN_MEMORY_MIN_BALANCE = new HashMap<>();
    private static final Map<String, Double> IN_MEMORY_INTEREST_RATE = new HashMap<>();

    static {
        IN_MEMORY_MIN_BALANCE.put("NEW", 10000.0);
        IN_MEMORY_MIN_BALANCE.put("STANDARD", 7500.0);
        IN_MEMORY_MIN_BALANCE.put("PREMIUM", 5000.0);
        IN_MEMORY_MIN_BALANCE.put("PRIVILEGE", 2500.0);

        IN_MEMORY_INTEREST_RATE.put("NEW", 2.70);
        IN_MEMORY_INTEREST_RATE.put("STANDARD", 3.00);
        IN_MEMORY_INTEREST_RATE.put("PREMIUM", 3.50);
        IN_MEMORY_INTEREST_RATE.put("PRIVILEGE", 4.00);
    }

    public AccountRulesEngine() {
        this.propertiesLoader = AccountRulesPropertiesLoader.getInstance();
    }

    public static synchronized AccountRulesEngine getInstance() {
        if (instance == null) {
            instance = new AccountRulesEngine();
        }
        return instance;
    }

    public static String getTenureBucket(int tenureYears) {
        if (tenureYears >= 5) {
            return "PRIVILEGE";
        } else if (tenureYears >= 3) {
            return "PREMIUM";
        } else if (tenureYears >= 1) {
            return "STANDARD";
        } else {
            return "NEW";
        }
    }

    // Static helper methods for Activity 13.1
    public static double getSavingsMinBalance(int tenureYears) {
        String bucket = getTenureBucket(tenureYears);
        if (instance != null) {
            Object val = instance.getAdditionalFeature("SAVINGS", tenureYears, "minBalance");
            if (val instanceof Number) {
                return ((Number) val).doubleValue();
            }
        }
        return IN_MEMORY_MIN_BALANCE.getOrDefault(bucket, 10000.0);
    }

    public static double getSavingsInterestRate(int tenureYears) {
        String bucket = getTenureBucket(tenureYears);
        if (instance != null) {
            Object val = instance.getAdditionalFeature("SAVINGS", tenureYears, "interestRate");
            if (val instanceof Number) {
                return ((Number) val).doubleValue();
            }
        }
        return IN_MEMORY_INTEREST_RATE.getOrDefault(bucket, 2.70);
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        return Math.max(25000.0, monthlyTurnover * 2.5);
    }

    public static double getFDInterestRate(int months) {
        if (months >= 12) {
            return 6.5;
        } else if (months >= 6) {
            return 5.5;
        } else {
            return 4.5;
        }
    }

    // STEP 8 — AccountRulesEngine.java -> getDailyTransferLimit(accountType, tenureYears)
    public Double getDailyTransferLimit(String accountType, int tenureYears) {
        Object limit = getAdditionalFeature(accountType, tenureYears, "dailyTransferLimit");
        if (limit == null) {
            return 0.0;
        }
        return (Double) limit;
    }

    public Object getAdditionalFeature(String accountType, int tenureYears, String featureName) {
        String bucket = getTenureBucket(tenureYears);

        // Special rule for CURRENT with 5+ years (PRIVILEGE): not in current.properties, returns null -> 0.0
        if ("CURRENT".equalsIgnoreCase(accountType) && "PRIVILEGE".equalsIgnoreCase(bucket)) {
            return null;
        }

        Object feature = propertiesLoader.getFeature(accountType, bucket, featureName);
        if (feature != null) {
            return feature;
        }

        // Fallback to in-memory tables if feature is minBalance or interestRate
        if ("SAVINGS".equalsIgnoreCase(accountType)) {
            if ("minBalance".equalsIgnoreCase(featureName)) {
                return IN_MEMORY_MIN_BALANCE.get(bucket);
            }
            if ("interestRate".equalsIgnoreCase(featureName)) {
                return IN_MEMORY_INTEREST_RATE.get(bucket);
            }
        }

        return null;
    }

    public AccountRulesPropertiesLoader getPropertiesLoader() {
        return propertiesLoader;
    }
}
