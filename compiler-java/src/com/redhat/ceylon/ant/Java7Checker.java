package com.redhat.ceylon.ant;

public class Java7Checker {

    public static void check() {
        String version = System.getProperty("java.version");
        if(version == null || version.isEmpty()){
            System.err.println("Unable to determine Java version (java.version property missing or empty). Aborting.");
            System.exit(1);
        }
        if(!version.startsWith("1.7")){
            System.err.println("Your Java version is not supported: "+version);
            System.err.println("Ceylon needs Java 7. Please install it from http://www.java.com");
            System.err.println("Aborting.");
            System.exit(1);
        }
    }
    
}
