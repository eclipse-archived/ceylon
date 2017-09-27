package org.eclipse.ceylon.common.log;

public class StderrLogger implements Logger {

    private boolean verbose;

    public StderrLogger(){
        this(false);
    }
    
    
    public StderrLogger(boolean verbose) {
        this.verbose = verbose;
    }


    @Override
    public void error(String str) {
        System.err.println("Error: " + str);
    }

    @Override
    public void warning(String str) {
        System.err.println("Warning: " + str);
    }

    @Override
    public void info(String str) {
        System.err.println("Info: " + str);
    }

    @Override
    public void debug(String str) {
        if(verbose)
            System.err.println("Debug: " + str);
    }

}
