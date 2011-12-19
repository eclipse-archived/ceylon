package com.redhat.ceylon.compiler.ant;

public class Module {
    
    public String name;
    public String version;
    
    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toSpec() {
        if(version == null)
            return name;
        return name + "/" + version;
    }
}
