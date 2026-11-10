package karob.bigtrees;

import java.io.*;
import java.util.*;

public class BTConfigFile {
    private final File file;
    private final Map<String, Map<String, String>> categories = new LinkedHashMap<>();

    public BTConfigFile(File file) {
        this.file = file;
    }

    public synchronized void load() {
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentCategory = "general";
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.endsWith("{")) {
                    currentCategory = line.substring(0, line.length() - 1).trim();
                    if (currentCategory.startsWith("\"") && currentCategory.endsWith("\"")) {
                        currentCategory = currentCategory.substring(1, currentCategory.length() - 1);
                    }
                    continue;
                }
                if (line.equals("}")) {
                    currentCategory = "general";
                    continue;
                }

                int eqIndex = line.indexOf('=');
                if (eqIndex != -1) {
                    String rawKey = line.substring(0, eqIndex).trim();
                    String val = line.substring(eqIndex + 1).trim();
                    if (rawKey.startsWith("S:") || rawKey.startsWith("I:") || rawKey.startsWith("D:") || rawKey.startsWith("B:")) {
                        rawKey = rawKey.substring(2).trim();
                    }
                    if (rawKey.startsWith("\"") && rawKey.endsWith("\"")) {
                        rawKey = rawKey.substring(1, rawKey.length() - 1);
                    }
                    categories.computeIfAbsent(currentCategory.toLowerCase(Locale.ROOT), k -> new LinkedHashMap<>()).put(rawKey.toLowerCase(Locale.ROOT), val);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized ConfigProperty get(String category, String key, Object defaultValue, String comment) {
        String catKey = category.toLowerCase(Locale.ROOT);
        String k = key.toLowerCase(Locale.ROOT);
        Map<String, String> cat = categories.computeIfAbsent(catKey, c -> new LinkedHashMap<>());
        if (!cat.containsKey(k)) {
            cat.put(k, String.valueOf(defaultValue));
        }
        return new ConfigProperty(this, catKey, k, cat.get(k), defaultValue);
    }

    public ConfigProperty get(String category, String key, String defaultValue) {
        return get(category, key, defaultValue, "");
    }

    public ConfigProperty get(String category, String key, int defaultValue) {
        return get(category, key, defaultValue, "");
    }

    public ConfigProperty get(String category, String key, String[] defaultValues, String comment) {
        String joined = String.join("\n", defaultValues);
        return get(category, key, joined, comment);
    }

    public synchronized void save() {
        try {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("# Configuration file");
                writer.println();
                for (Map.Entry<String, Map<String, String>> entry : categories.entrySet()) {
                    writer.println("\"" + entry.getKey() + "\" {");
                    for (Map.Entry<String, String> prop : entry.getValue().entrySet()) {
                        String val = prop.getValue();
                        if (val.contains("\n")) {
                            writer.println("    S:\"" + prop.getKey() + "\" <");
                            for (String sub : val.split("\n")) {
                                writer.println("        " + sub);
                            }
                            writer.println("     >");
                        } else {
                            writer.println("    S:\"" + prop.getKey() + "\"=" + val);
                        }
                    }
                    writer.println("}");
                    writer.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ConfigProperty {
        private final BTConfigFile parent;
        private final String category;
        private final String key;
        private String value;
        private final Object defaultValue;

        public ConfigProperty(BTConfigFile parent, String category, String key, String value, Object defaultValue) {
            this.parent = parent;
            this.category = category;
            this.key = key;
            this.value = value;
            this.defaultValue = defaultValue;
        }

        public String getString() {
            return value != null ? value : String.valueOf(defaultValue);
        }

        public int getInt() {
            try {
                return Integer.parseInt(getString().trim());
            } catch (Exception e) {
                if (defaultValue instanceof Number) {
                    return ((Number) defaultValue).intValue();
                }
                return 0;
            }
        }

        public double getDouble() {
            try {
                return Double.parseDouble(getString().trim());
            } catch (Exception e) {
                if (defaultValue instanceof Number) {
                    return ((Number) defaultValue).doubleValue();
                }
                return 0.0;
            }
        }

        public boolean getBoolean() {
            return Boolean.parseBoolean(getString().trim());
        }

        public String[] getStringList() {
            if (value == null || value.isEmpty()) {
                if (defaultValue instanceof String[]) {
                    return (String[]) defaultValue;
                }
                return new String[0];
            }
            return value.split("\n");
        }
    }
}
