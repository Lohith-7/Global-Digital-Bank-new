# Activity 21: Repository Pattern (Interfaces + InMemory + Factory)

## Overview
This activity introduces the Repository Pattern, decoupling domain models and the service layer from data persistence mechanisms. In this activity, students define repository interfaces for accounts and transactions, build in-memory implementations, configure a `RepositoryFactory` driven by `persistence.properties`, and refactor `AccountService` to use repositories.

## Files Included
- `src/com/gdb/repository/AccountRepository.java` — contract for account persistence operations
- `src/com/gdb/repository/TransactionRepository.java` — contract for transaction persistence operations
- `src/com/gdb/repository/InMemoryAccountRepository.java` — in-memory `Map<Integer, IAccount>` implementation
- `src/com/gdb/repository/InMemoryTransactionRepository.java` — in-memory transaction storage
- `src/com/gdb/repository/RepositoryFactory.java` — factory selecting `memory`, `jdbc`, or `file`
- `src/main/resources/config/persistence.properties` — configuration for the active persistence mode
- `src/com/gdb/service/AccountService.java` — service refactored to use repository interfaces
- `src/com/gdb/tests/TestRepositoryInMemory.java` — repository test driver

## Main Idea
The service layer should no longer directly manage a `Map` or list of data. Instead, repositories provide an abstraction for storage logic, and the factory chooses the implementation from configuration.

## Example Execution
```bash
javac -d out/production/Global-Digital-Bank-GDB-main $(find src -name "*.java")
java -cp out/production/Global-Digital-Bank-GDB-main com.gdb.tests.TestRepositoryInMemory
```

## Expected Outcome
- `persistence.mode=memory` uses the in-memory repository implementation
- Account CRUD operations work through repository interfaces
- Transactions can be stored and searched by account number
- `AccountService` uses repositories instead of directly holding in-memory data
