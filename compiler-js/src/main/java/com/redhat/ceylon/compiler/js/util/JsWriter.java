package com.redhat.ceylon.compiler.js.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.redhat.ceylon.compiler.js.GenerateJsVisitor;

/** The component that writes the generated code.
 * 
 * @author Enrique Zamudio
 */
public class JsWriter {

    private final boolean minify;
    private final PrintWriter verboseOut;
    private Writer out;
    private Writer originalOut;
    /** counter to avoid empty lines */
    private int nlcount=0;

    public JsWriter(Writer output, PrintWriter verboseOut, boolean minify) {
        out = output;
        this.verboseOut = verboseOut;
        this.minify = minify;
        originalOut = out;
    }

    /** Print generated code to the Writer specified at creation time.
     * Automatically prints indentation first if necessary.
     * @param code The main code
     * @param codez Optional additional strings to print after the main code. */
    public void write(String code, String... codez) {
        try {
            if (!"\n".equals(code))nlcount=0;
            out.write(code);
            for (String s : codez) {
                out.write(s);
            }
            if (verboseOut != null && out == originalOut) {
                //Print code to console (when printing to REAL output)
                verboseOut.print(code);
                for (String s : codez) {
                    verboseOut.print(s);
                }
                verboseOut.flush();
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Generating JS code", ioe);
        }
    }

    /** Prints a newline. Indentation will automatically be printed by {@link #out(String, String...)}
     * when the next line is started.
     * @param semicolon  if <code>true</code> then a semicolon is printed at the end
     *                  of the previous line*/
    public void endLine(boolean semicolon) {
        if (semicolon) {
            write(";");
            if (minify)return;
        }
        nlcount++;
        if (nlcount>1)return;
        write("\n");
    }
    /** Increases indentation level, prints opening brace and newline. Indentation will
     * automatically be printed by {@link #out(String, String...)} when the next line is started. */
    public void beginBlock() {
        write("{");
        if (!minify)endLine(false);
    }

    /** Decreases indentation level and prints a closing brace in new line (using
     * {@link #endLine()}).
     * @param semicolon  if <code>true</code> then prints a semicolon after the brace
     * @param newline  if <code>true</code> then additionally calls {@link #endLine()} */
    public void endBlock(boolean semicolon, boolean newline) {
        if (!minify)endLine(false);
        write(semicolon ? "};" : "}");
        if (semicolon&&minify)return;
        if (newline) { endLine(false); }
    }

    public void spitOut(String s) {
        if (verboseOut == null) {
            System.out.println(s);
        } else {
            verboseOut.println(s);
        }
    }

    public String generateToString(final GenerateJsVisitor.GenerateCallback callback) {
        final Writer oldWriter = out;
        out = new StringWriter();
        callback.generateValue();
        final String str = out.toString();
        out = oldWriter;
        return str;
    }

}
