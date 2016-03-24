package com.redhat.ceylon.compiler.java.runtime.model;

/**
 * Contract to determine the runtime version of a module 
 * from the compile time version.
 */
public interface RuntimeResolver {

    /**
     * Resolve the version of a module being used at runtime.
     * @param moduleName The module name  
     * @param moduleVersion The compile-time version
     * @return The runtime version
     */
    String resolveVersion(String moduleName, String moduleVersion);
    
}
