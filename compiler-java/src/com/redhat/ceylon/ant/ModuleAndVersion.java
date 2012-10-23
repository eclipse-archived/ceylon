package com.redhat.ceylon.ant;

public class ModuleAndVersion extends Module {

    public String version;
    
    public ModuleAndVersion() {
    }
    
    public ModuleAndVersion(String spec) {
        int index = spec.indexOf('/');
        if (index != -1) {
            name = spec.substring(0, index);
            version = spec.substring(index + 1);
        } else {
            name = spec;
        }
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
