package com.redhat.ceylon.common.tool;


/**
 * Helper methods for tools
 * @author tom
 */
public class Tools {

    private Tools() {}
    
    public static String progName() {
        return System.getProperty("com.redhat.ceylon.common.tool.progname", "ceylon");
    }

}
