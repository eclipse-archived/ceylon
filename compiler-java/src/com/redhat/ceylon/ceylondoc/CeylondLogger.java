package com.redhat.ceylon.ceylondoc;

import com.redhat.ceylon.cmr.api.Logger;

public class CeylondLogger implements Logger {

    private int errors;
    
    public int getErrors(){
        return errors;
    }

    @Override
    public void error(String str) {
        errors++;
        System.err.println("Error: "+str);
    }

    @Override
    public void warning(String str) {
        System.err.println("Warning: "+str);
    }

    @Override
    public void info(String str) {
        System.err.println("Note: "+str);
    }

    @Override
    public void debug(String str) {
        //System.err.println("Debug: "+str);
    }

}
