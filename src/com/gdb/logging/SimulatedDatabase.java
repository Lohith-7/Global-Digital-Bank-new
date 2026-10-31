package com.gdb.logging;

import java.util.*;

/**
 * A simple in-memory simulated database using a Map of table names to lists of objects.
 */
public class SimulatedDatabase {
    private final Map<String, List<Object>> tables = new HashMap<>();

    public void insert(String tableName, Object record) {
        tables.computeIfAbsent(tableName, k -> new ArrayList<>()).add(record);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> selectAll(String tableName) {
        List<Object> records = tables.getOrDefault(tableName, new ArrayList<>());
        List<T> result = new ArrayList<>();
        for (Object obj : records) {
            result.add((T) obj);
        }
        return result;
    }

    public void deleteAll(String tableName) {
        tables.remove(tableName);
    }
}
