package org.eclipse.ceylon.common;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/*
 * Really crappy proxy class for org.eclipse.ceylon.compiler.ModuleDescriptorReader 
 * to prevent a problem with that class accessing all kinds of needed dependencies
 * before we have been able to configure our class loader with the required paths
 */
public class ModuleDescriptorReader {
    private Object instance;
    private Method moduleVersion;
    private Method moduleName;
    private Method moduleBackend;
    private Method moduleLicense;
    private Method moduleAuthors;
    private Method moduleImports;
    private Method moduleGroupId;
    private Method moduleArtifactId;
    private Method moduleLabel;

    // WARNING: this exception class name is used in org.eclipse.ceylon.ant.ModuleDescriptorReader because of 
    // classpath issues in the ant task. If you ever manage to untangle this mess: BE MY GUEST. 
    @SuppressWarnings("serial")
    public static class NoSuchModuleException extends Exception {

        public NoSuchModuleException(String string) {
            super(string);
        }
    }

    public ModuleDescriptorReader(String moduleName, File srcDir) throws NoSuchModuleException {
        this(ModuleDescriptorReader.class.getClassLoader(), moduleName, srcDir);
    }

    public ModuleDescriptorReader(ClassLoader cl, String moduleName, File srcDir) throws NoSuchModuleException {
        try{
            Class<?> mdr = cl.loadClass("org.eclipse.ceylon.compiler.ModuleDescriptorReader");
            this.moduleVersion = mdr.getMethod("getModuleVersion");
            this.moduleVersion.setAccessible(true);
            this.moduleName = mdr.getMethod("getModuleName");
            this.moduleName.setAccessible(true);
            this.moduleBackend = mdr.getMethod("getModuleBackends");
            this.moduleBackend.setAccessible(true);
            this.moduleLicense = mdr.getMethod("getModuleLicense");
            this.moduleLicense.setAccessible(true);
            this.moduleAuthors = mdr.getMethod("getModuleAuthors");
            this.moduleAuthors.setAccessible(true);
            this.moduleImports = mdr.getMethod("getModuleImports");
            this.moduleImports.setAccessible(true);
            this.moduleGroupId = mdr.getMethod("getModuleGroupId");
            this.moduleGroupId.setAccessible(true);
            this.moduleArtifactId = mdr.getMethod("getModuleArtifactId");
            this.moduleArtifactId.setAccessible(true);
            this.moduleLabel = mdr.getMethod("getModuleLabel");
            this.moduleLabel.setAccessible(true);
            Constructor<?> constructor = mdr.getConstructor(String.class, File.class);
            constructor.setAccessible(true);
            this.instance = constructor.newInstance(moduleName, srcDir);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if(cause != null && cause.getClass().getName().equals("org.eclipse.ceylon.common.ModuleDescriptorReader$NoSuchModuleException"))
                throw new NoSuchModuleException(cause.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the module version
     * @return The module version, or null if no version could be found
     */
    public String getModuleVersion() {
        try {
            return (String)moduleVersion.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the module name
     * @return The module name, or null if no name could be found
     */
    public String getModuleName() {
        try {
            return (String)moduleName.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the module label
     * @return The module label, or null if no label could be found
     */
    public String getModuleLabel() {
        try {
            return (String)moduleLabel.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the module backends
     * @return A list of names of the supported backends
     */
    @SuppressWarnings("unchecked")
    public List<String> getModuleBackends() {
        try {
            return (List<String>)moduleBackend.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the module license
     * @return The module version, or null if no version could be found
     */
    public String getModuleLicense() {
        try {
            return (String)moduleLicense.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the module authors
     * @return The list of module authors, or empty list of no authors could be found
     */
    @SuppressWarnings("unchecked")
    public List<String> getModuleAuthors() {
        try {
            return (List<String>)moduleAuthors.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the module imports. Format is [repoType, name, version, optional, shared]
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getModuleImports() {
        try {
            return (List<Object[]>)moduleImports.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getModuleArtifactId() {
        try {
            return (String)moduleArtifactId.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getModuleGroupId() {
        try {
            return (String)moduleGroupId.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
