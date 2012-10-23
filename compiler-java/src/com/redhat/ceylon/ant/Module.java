package com.redhat.ceylon.ant;

import java.io.File;

public class Module {
    
    public String name;
    
    public Module() {
        super();
    }
    
    public Module(String name) {
        this.name = name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String toSpec() {
        return name;
    }
    
    @Override
    public String toString() {
        return toSpec();
    }
    
    public File toDir() {
        return new File(name.replace(".", "/"));
    }
}
