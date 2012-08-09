package com.redhat.ceylon.tools.annotation;

import java.util.Iterator;

public interface Style {

    public boolean isEoo(String arg);

    public boolean isLongForm(String arg);
    
    public String getLongFormOption(String arg);
    public String getLongFormArgument(String arg, Iterator<String> iter);

    public boolean isShortForm(String arg);

    public boolean isArgument(String arg);
    
}