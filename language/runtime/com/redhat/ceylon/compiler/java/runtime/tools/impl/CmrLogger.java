package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import com.redhat.ceylon.common.log.Logger;

public class CmrLogger implements Logger {

    private boolean debugEnabled;

    public CmrLogger(boolean verbose) {
        debugEnabled = verbose;
    }

    @Override
    public void error(String str) {
        System.err.println("[CMR:ERROR] "+str);
    }

    @Override
    public void warning(String str) {
        System.err.println("[CMR:WARNING] "+str);
    }

    @Override
    public void info(String str) {
        System.err.println("[CMR:INFO] "+str);
    }

    @Override
    public void debug(String str) {
        if(debugEnabled)
            System.err.println("[CMR:DEBUG] "+str);
    }

}
