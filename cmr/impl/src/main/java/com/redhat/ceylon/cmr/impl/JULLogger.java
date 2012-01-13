package com.redhat.ceylon.cmr.impl;

import com.redhat.ceylon.cmr.api.Logger;

public class JULLogger implements Logger {

    static java.util.logging.Logger log = java.util.logging.Logger.getLogger("com.redhat.ceylon.cmr");
    
    @Override
    public void error(String str) {
        log.severe(str);
    }

    @Override
    public void warning(String str) {
        log.warning(str);
    }

    @Override
    public void info(String str) {
        log.info(str);
    }

    @Override
    public void debug(String str) {
        log.fine(str);
    }

}
