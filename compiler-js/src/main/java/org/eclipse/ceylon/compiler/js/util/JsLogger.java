/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.util;

import java.io.PrintWriter;
import java.io.Writer;

import org.eclipse.ceylon.common.log.Logger;

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
