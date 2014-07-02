package com.redhat.ceylon.compiler.java.tools;

import org.antlr.runtime.ANTLRStringStream;

/**
 * An {@code ANTLRStringStream} which understands mixed line endings in 
 * its line number counts.
 * 
 * @see <a href="https://github.com/ceylon/ceylon-compiler/issues/1712">#1712</a>
 */
public class NewlineFixingInputStream extends
        ANTLRStringStream {
   
    public NewlineFixingInputStream() {
        super();
    }

    public NewlineFixingInputStream(char[] data, int numberOfActualCharsInArray) {
        super(data, numberOfActualCharsInArray);
    }

    public NewlineFixingInputStream(String input) {
        super(input);
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