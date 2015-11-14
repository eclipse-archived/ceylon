package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.IOException;
import java.io.Writer;

/**
 * Writer which doesn't write anything.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class NullWriter extends Writer {

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
    }

}
