package com.redhat.ceylon.compiler.java.tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.antlr.runtime.ANTLRStringStream;

/**
 * <p>An {@code ANTLRStringStream} which understands mixed line endings in 
 * its line number counts.</p>
 * 
 * <p>Factory methods {@link #fromStream(InputStream, String)} 
 * and {@link #fromReader(Reader)} are provided if you have 
 * a stream-like thing rather than a {@code String}.</p>
 * 
 * @see <a href="https://github.com/ceylon/ceylon-compiler/issues/1712">#1712</a>
 */
public class NewlineFixingStringStream extends
        ANTLRStringStream {
    
    private static final int DEFAULT_INIT_BUF_SIZE = 1024;
    
    
    public NewlineFixingStringStream() {
        super();
    }

    public NewlineFixingStringStream(char[] data, int numberOfActualCharsInArray) {
        super(data, numberOfActualCharsInArray);
    }

    public NewlineFixingStringStream(String input) {
        super(input);
    }
    
    /** 
     * Produces a {@code NewlineFixingStringStream} instance by 
     * reading all the {@code encoding}-encoded characters from 
     * the given {@code input} stream.
     * 
     * @param input The stream to read. This will be closed by side-effect.
     * @param encoding How the characters in the given stream are encoded.
     */
    public static NewlineFixingStringStream fromStream(InputStream input, 
            String encoding)
            throws IOException {
        return fromStream(input, encoding, DEFAULT_INIT_BUF_SIZE);
    }
    /** 
     * Produces a {@code NewlineFixingStringStream} instance by 
     * reading all the {@code encoding}-encoded characters from 
     * the given {@code input} stream.
     * 
     * @param input The stream to read. This will be closed by side-effect.
     * @param encoding How the characters in the given stream are encoded.
     * @param bufSize The initial size of the char data buffer
     */
    public static NewlineFixingStringStream fromStream(InputStream input, String encoding, int bufSize) 
            throws IOException {
        return fromReader(new InputStreamReader(new BufferedInputStream(input), encoding), bufSize);
    }
    /** 
     * Produces a {@code NewlineFixingStringStream} instance by 
     * reading all the characters from 
     * the given {@code reader}.
     * 
     * @param reader The reader to read. This will be closed by side-effect.
     */
    public static NewlineFixingStringStream fromReader(Reader reader) 
            throws IOException {
        return fromReader(reader, DEFAULT_INIT_BUF_SIZE);
    }
    /** 
     * Produces a {@code NewlineFixingStringStream} instance by 
     * reading all the characters from 
     * the given {@code reader}.
     * 
     * @param reader The reader to read. This will be closed by side-effect.
     * @param bufSize The initial size of the char data buffer
     */
    public static NewlineFixingStringStream fromReader(Reader reader, int bufSize) 
            throws IOException {
        try {
            char[] data = new char[bufSize];
            int read = reader.read(data);
            int n = 0;
            while (read != -1) {
                n += read;
                if (n == data.length) {
                    char[] newdata = new char[data.length*2];
                    System.arraycopy(data, 0, newdata, 0, n);
                    data = newdata;
                }
                read = reader.read(data, n, data.length-n);
            }
            return new NewlineFixingStringStream(data, n);
        } finally {
            reader.close();
        }
    }
    
    @Override
    public void consume() {
        /*
         * We override this so that \n, \r\n and \r each count as one newline 
         */
        if ( p < n ) {
            charPositionInLine++;
            if (data[p]=='\n') {
                line++;
                charPositionInLine=0;
            } else if (data[p]=='\r') {
                if (p+1 == n || data[p+1]!='\n') {
                    line++;
                    charPositionInLine=0;
                }
            }
            p++;
        }
    }
}