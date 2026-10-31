package com.gdb.repository;

import java.io.InputStream;
import java.util.Properties;

public class RepositoryFactory {
    private RepositoryFactory() {
    }

    public static String getPersistenceMode() {
        return loadPersistenceMode();
    }

    public static AccountRepository getAccountRepository() {
        return createAccountRepository();
    }

    public static TransactionRepository getTransactionRepository() {
        return createTransactionRepository();
    }

    public static AccountRepository createAccountRepository() {
        String mode = loadPersistenceMode();

        switch (mode) {
            case "memory":
                return new InMemoryAccountRepository();
            case "jdbc":
                throw new UnsupportedOperationException("JDBC repository not implemented yet. Activity 22+");
            case "file":
                throw new UnsupportedOperationException("File repository not implemented yet. Activity 23+");
            default:
                throw new IllegalArgumentException("Unsupported persistence mode: " + mode);
        }
    }

    public static TransactionRepository createTransactionRepository() {
        String mode = loadPersistenceMode();

        switch (mode) {
            case "memory":
                return new InMemoryTransactionRepository();
            case "jdbc":
                throw new UnsupportedOperationException("JDBC repository not implemented yet. Activity 22+");
            case "file":
                throw new UnsupportedOperationException("File repository not implemented yet. Activity 23+");
            default:
                throw new IllegalArgumentException("Unsupported persistence mode: " + mode);
        }
    }

    private static String loadPersistenceMode() {
        Properties properties = new Properties();
        try (InputStream inputStream = RepositoryFactory.class.getClassLoader()
                .getResourceAsStream("config/persistence.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read persistence configuration.", e);
        }

        String mode = properties.getProperty("persistence.mode", "memory");
        return mode.trim().toLowerCase();
    }
}
