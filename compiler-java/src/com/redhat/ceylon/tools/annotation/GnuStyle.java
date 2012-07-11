package com.redhat.ceylon.tools.annotation;

import java.util.Iterator;

public class GnuStyle implements Style {
    private static final String SHORT_PREFIX = "-";
    private static final char LONG_SEP = '=';
    private static final String LONG_PREFIX = "--";

    public  boolean isEoo(final String arg) {
        return arg.equals(LONG_PREFIX);
    }
    
    public  boolean isLongForm(final String arg) {
        return arg.startsWith(LONG_PREFIX);
    }
    
    public String getLongFormOption(final String arg) {
        final int eq = arg.indexOf(LONG_SEP);
        String longName;
        if (eq == -1) { // long-form option
            longName = arg.substring(LONG_PREFIX.length());
        } else {// long-form option argument
            longName = arg.substring(LONG_PREFIX.length(), eq);
        }
        return longName;
    }
    
    public String getLongFormArgument(final String arg, Iterator<String> iter) {
        final int eq = arg.indexOf(LONG_SEP);
        String argument = arg.substring(eq+1);
        return argument;
    }

    public boolean isShortForm(String arg) {
        return arg.startsWith(SHORT_PREFIX) && !arg.equals(SHORT_PREFIX);
    }

    @Override
    public boolean isArgument(String arg) {
        return true;
    }

}