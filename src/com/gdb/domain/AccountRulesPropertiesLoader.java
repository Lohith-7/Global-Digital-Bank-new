package com.gdb.domain;

import java.io.*;
import java.util.*;

public class AccountRulesPropertiesLoader {
    private static AccountRulesPropertiesLoader instance;
    private final Map<String, Properties> propertiesMap = new HashMap<>();
    private final Map<String, Map<String, Map<String, Object>>> structuredRules = new HashMap<>();

    public AccountRulesPropertiesLoader() {
        loadAllRules();
    }

    public static synchronized AccountRulesPropertiesLoader getInstance() {
        if (instance == null) {
            instance = new AccountRulesPropertiesLoader();
        }
        return instance;
    }

    public void loadAllRules() {
        String[] types = {"savings", "current", "fixeddeposit", "salary"};
        for (String type : types) {
            loadRules(type);
        }
    }

    public Properties loadRules(String accountType) {
        String normalizedType = accountType.toLowerCase().trim();
        Properties props = new Properties();

        // Search locations: file system and classpath
        String[] possiblePaths = {
            "src/main/resources/config/rules/" + normalizedType + ".properties",
            "src/main/resources/config/" + normalizedType + ".properties",
            "bin/config/rules/" + normalizedType + ".properties",
            "config/rules/" + normalizedType + ".properties",
            normalizedType + ".properties"
        };

        boolean loaded = false;
        for (String pathStr : possiblePaths) {
            File f = new File(pathStr);
            if (f.exists() && f.isFile()) {
                try (InputStream in = new FileInputStream(f)) {
                    props.load(in);
                    loaded = true;
                    break;
                } catch (IOException ignored) {}
            }
        }

        if (!loaded) {
            // Try classpath
            InputStream stream = getClass().getResourceAsStream("/config/rules/" + normalizedType + ".properties");
            if (stream == null) {
                stream = getClass().getClassLoader().getResourceAsStream("config/rules/" + normalizedType + ".properties");
            }
            if (stream != null) {
                try (InputStream in = stream) {
                    props.load(in);
                    loaded = true;
                } catch (IOException ignored) {}
            }
        }

        propertiesMap.put(normalizedType, props);
        parseStructuredRules(normalizedType, props);
        return props;
    }

    private void parseStructuredRules(String accountType, Properties props) {
        String[] buckets = {"new", "standard", "premium", "privilege"};
        Map<String, Map<String, Object>> bucketMap = structuredRules.computeIfAbsent(accountType.toUpperCase(), k -> new HashMap<>());

        for (String bucket : buckets) {
            Map<String, Object> featureMap = bucketMap.computeIfAbsent(bucket.toUpperCase(), k -> new HashMap<>());

            String minBalKey = "min.balance." + bucket;
            if (props.containsKey(minBalKey)) {
                featureMap.put("minBalance", Double.parseDouble(props.getProperty(minBalKey)));
            }

            String interestKey = "interest.rate." + bucket;
            if (props.containsKey(interestKey)) {
                featureMap.put("interestRate", Double.parseDouble(props.getProperty(interestKey)));
            }

            String overdraftKey = "overdraft.limit." + bucket;
            if (props.containsKey(overdraftKey)) {
                featureMap.put("overdraftLimit", Double.parseDouble(props.getProperty(overdraftKey)));
            }

            String dailyLimitKey = "daily.transfer.limit." + bucket;
            if (props.containsKey(dailyLimitKey)) {
                featureMap.put("dailyTransferLimit", Double.parseDouble(props.getProperty(dailyLimitKey)));
            }
        }
    }

    public Object getFeature(String accountType, String tenureBucket, String featureName) {
        if (accountType == null || tenureBucket == null || featureName == null) {
            return null;
        }
        Map<String, Map<String, Object>> typeMap = structuredRules.get(accountType.toUpperCase().trim());
        if (typeMap == null) {
            return null;
        }
        Map<String, Object> bucketMap = typeMap.get(tenureBucket.toUpperCase().trim());
        if (bucketMap == null) {
            return null;
        }
        return bucketMap.get(featureName);
    }

    public String getProperty(String accountType, String key, String defaultValue) {
        Properties props = propertiesMap.get(accountType.toLowerCase().trim());
        if (props == null) {
            return defaultValue;
        }
        return props.getProperty(key, defaultValue);
    }

    public double getDouble(String accountType, String key, double defaultValue) {
        String val = getProperty(accountType, key, null);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public Set<String> getLoadedAccountTypes() {
        return structuredRules.keySet();
    }

    public int getBucketCount(String accountType) {
        Map<String, Map<String, Object>> m = structuredRules.get(accountType.toUpperCase().trim());
        return m != null ? m.size() : 0;
    }
}
