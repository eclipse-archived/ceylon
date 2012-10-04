package com.redhat.ceylon.common;

public class Versions {
    
    public static final int CEYLON_VERSION_MAJOR = 0;
    public static final int CEYLON_VERSION_MINOR = 4;
    public static final int CEYLON_VERSION_RELEASE = 0;
    public static final String CEYLON_VERSION_NUMBER = CEYLON_VERSION_MAJOR + "." + CEYLON_VERSION_MINOR /* + "." + CEYLON_VERSION_RELEASE */;
    public static final String CEYLON_VERSION_NAME = "Supercalifragilisticexpialidocious";
    public static final String CEYLON_VERSION = CEYLON_VERSION_NUMBER + " (" + CEYLON_VERSION_NAME + ")";

    /**
     * M1 and M2 are 0.0 since they were not tagged at the time
     * M3 is 1.0 as the first version with binary version information
     * M3.1 is 2.0
     * M4 is 3.0
     */
    public static final int JVM_BINARY_MAJOR_VERSION = 3;
    public static final int JVM_BINARY_MINOR_VERSION = 0;

}
