package com.redhat.ceylon.tools.annotation;

import java.util.Iterator;

public class JavacStyle implements Style {

    private static final String LONG_PREFIX = "-";

    @Override
    public boolean isEoo(String arg) {
        return false;
    }

    @Override
    public boolean isLongForm(String arg) {
        return arg.startsWith(LONG_PREFIX);
    }

    @Override
    public boolean isShortForm(String arg) {
        return false;
    }

    @Override
    public boolean isArgument(String arg) {
        return true;
    }

    @Override
    public String getLongFormOption(String arg) {
        int idx = arg.indexOf('=');
        if (idx == -1) {
            return arg.substring(LONG_PREFIX.length());
        } else {
            return arg.substring(LONG_PREFIX.length(), idx);
        }
    }

    @Override
    public String getLongFormArgument(String arg, Iterator<String> iter) {
        int idx = arg.indexOf('=');
        if (idx == -1) {
            return iter.next();
        } else {
            return arg.substring(idx+1);
        }
    }
    
}