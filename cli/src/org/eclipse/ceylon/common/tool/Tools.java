package org.eclipse.ceylon.common.tool;


/**
 * Helper methods for tools
 * @author tom
 */
public class Tools {

    private Tools() {}
    
    public static String progName() {
        return System.getProperty("org.eclipse.ceylon.common.tool.progname", "ceylon");
    }

}
