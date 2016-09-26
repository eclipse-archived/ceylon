package com.redhat.ceylon.model.runtime;

import com.redhat.ceylon.model.cmr.RuntimeResolver;

/**
 * This interface is here just so the language module can use a special hook in the ceylon-runtime project
 * without having to depend on it, which would be a circular dependency. Otherwise it would belong in the runtime
 * project.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface CeylonModuleClassLoader {
    
    @SuppressWarnings("serial")
    public static class ModuleLoadException extends Exception {
        public ModuleLoadException(Exception cause) {
            super(cause);
        }
    }
    
    public void registerInMetaModel();
    public String getModuleName();
    public String getModuleVersion();
    public RuntimeResolver getRuntimeResolver();
    public CeylonModuleClassLoader loadModule(String name, String version) throws ModuleLoadException;
}
