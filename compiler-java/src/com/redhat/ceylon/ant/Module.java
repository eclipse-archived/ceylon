package com.redhat.ceylon.ant;

import java.io.File;

/**
 * Represents a module with a name and optionally a version.
 * @author tom
 */
public class Module {
    
    private String name;
    
    private String version;
    
    public Module() {
        this(null);
    }
    
    public Module(String name) {
        this(name, null);
    }
    
    public Module(String name, String version) {
        super();
        setName(name);
        setVersion(version);
    }
    
    public static Module fromSpec(String spec) {
        int index = spec.indexOf('/');
        String name;
        String version;
        if (index != -1) {
            name = spec.substring(0, index);
            version = spec.substring(index + 1);
        } else {
            name = spec;
            version = null;
        }
        return new Module(name, version);
    }

    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String toSpec() {
        if (version == null) {
            return name;
        }
        return name + "/" + version;
    }
    
    public String toVersionlessSpec() {
        return name;
    }
    
    public String toVersionedSpec() {
        if (version == null) {
            throw new RuntimeException("Module " + name + " doesn't specify a version");
        }
        return name + "/" + version;
    }

    @Override
    public String toString() {
        return toSpec();
    }
    
    public File toDir() {
        return new File(name.replace(".", "/"));
    }
}
