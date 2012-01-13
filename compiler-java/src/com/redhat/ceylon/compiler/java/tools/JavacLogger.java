package com.redhat.ceylon.compiler.java.tools;

import com.redhat.ceylon.cmr.api.Logger;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;

public class JavacLogger implements Logger {

    private boolean debugEnabled;
    private Log log;

    public JavacLogger(Options options, Log log) {
        this.debugEnabled = options.get(OptionName.VERBOSE) != null;
        this.log = log;
    }

    @Override
    public void error(String str) {
        log.error("ceylon", str);
    }

    @Override
    public void warning(String str) {
        log.warning("ceylon", str);
    }

    @Override
    public void info(String str) {
        log.note("ceylon", str);
    }

    @Override
    public void debug(String str) {
        if(debugEnabled)
            log.printLines(log.errWriter, str);
    }

}
