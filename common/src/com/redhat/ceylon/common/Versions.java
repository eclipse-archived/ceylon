package com.redhat.ceylon.common;

public class Versions {
    
    // The current version is Ceylon 0.6.1 "Virtual Boy"
    // This comment is here so this file will show up in searches for the current version number
    
    // WARNING Don't forget to update ceylon.language/runtime-js/process.js when you change anything here!

    public static final int CEYLON_VERSION_MAJOR = 0;
    public static final int CEYLON_VERSION_MINOR = 6;
    public static final int CEYLON_VERSION_RELEASE = 1;
    public static final String CEYLON_VERSION_NUMBER = CEYLON_VERSION_MAJOR + "." + CEYLON_VERSION_MINOR + "." + CEYLON_VERSION_RELEASE;
    public static final String CEYLON_VERSION_NAME = "Virtual Boy";
    public static final String CEYLON_VERSION = CEYLON_VERSION_NUMBER + " (" + CEYLON_VERSION_NAME + ")";

    /**
     * M1 and M2 are 0.0 since they were not tagged at the time
     * M3 is 1.0 as the first version with binary version information
     * M3.1 is 2.0
     * M4 is 3.0
     * M5 is 4.0
     * M6 is 5.0
     */
    public static final int JVM_BINARY_MAJOR_VERSION = 5;
    public static final int JVM_BINARY_MINOR_VERSION = 0;

}
