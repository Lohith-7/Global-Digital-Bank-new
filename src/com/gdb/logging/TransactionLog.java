package com.gdb.logging;

import com.gdb.command.TransactionCommand;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionLog {
    private static final String DATA_DIR = "data";
    private static final String FILE_PATH = DATA_DIR + File.separator + "transactions.ser";

    // 📝 STEP 7 — log(TransactionCommand cmd)
    public void log(TransactionCommand cmd) {
        // Ensure data/ directory exists
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(FILE_PATH);
        ObjectOutputStream oos = null;
        try {
            if (!file.exists()) {
                // New file — use regular ObjectOutputStream
                oos = new ObjectOutputStream(new FileOutputStream(file));
            } else {
                // Existing file — use AppendableObjectOutputStream to avoid duplicate stream headers
                oos = new AppendableObjectOutputStream(new FileOutputStream(file, true));
            }
            oos.writeObject(cmd);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error logging transaction: " + e.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ignored) {}
            }
        }
    }

    // 📝 STEP 8 — readAll()
    public List<TransactionCommand> readAll() {
        List<TransactionCommand> commands = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return commands;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof TransactionCommand) {
                        commands.add((TransactionCommand) obj);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // End of stream or error
        }
        return commands;
    }

    // 📝 STEP 9 — clear()
    public void clear() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
}
