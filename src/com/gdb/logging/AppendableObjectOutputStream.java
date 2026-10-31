package com.gdb.logging;

import java.io.*;

/**
 * An ObjectOutputStream subclass that suppresses the stream header
 * when appending to an existing file.
 */
public class AppendableObjectOutputStream extends ObjectOutputStream {

    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // Do not write a header — prevents duplicate stream headers when appending
        reset();
    }
}
