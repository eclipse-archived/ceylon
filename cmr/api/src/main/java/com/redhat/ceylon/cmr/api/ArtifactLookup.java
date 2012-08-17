package com.redhat.ceylon.cmr.api;

public class ArtifactLookup {
    private String name;
    private Type type;

    public enum Type {
        SRC, JVM, JS
    }
    
    public ArtifactLookup(String name, Type type){
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
