package com.redhat.ceylon.cmr.api;

public interface Logger {
    void error(String str);
    void warning(String str);
    void info(String str);
    void debug(String str);
}
