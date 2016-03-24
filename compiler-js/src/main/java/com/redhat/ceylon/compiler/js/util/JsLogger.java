package com.redhat.ceylon.compiler.js.util;

import java.io.PrintWriter;
import java.io.Writer;

import com.redhat.ceylon.common.log.Logger;

public class JsLogger implements Logger {

    private PrintWriter out;
    private boolean verbose;
    
    public JsLogger(Options opts) {
        Writer outWriter = opts.getOutWriter();
        if(outWriter == null)
            out = new PrintWriter(System.err, true);
        else
            out = new PrintWriter(outWriter, true);
        verbose = opts.isVerbose();
    }

    @Override
    public void error(String str) {
        out.print("Error: ");
        out.println(str);
    }

    @Override
    public void warning(String str) {
        out.print("Warning: ");
        out.println(str);
    }

    @Override
    public void info(String str) {
        out.print("Note: ");
        out.println(str);
    }

    @Override
    public void debug(String str) {
        if(verbose){
            out.print("[");
            out.print(str);
            out.println("]");
        }
    }

}
