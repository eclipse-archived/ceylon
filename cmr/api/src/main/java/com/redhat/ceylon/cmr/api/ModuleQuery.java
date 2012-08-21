package com.redhat.ceylon.cmr.api;

public class ModuleQuery {
    private String name;
    private Type type;

    public enum Type {
        SRC(ArtifactContext.SRC), 
        JVM(ArtifactContext.CAR, ArtifactContext.JAR), 
        JS(ArtifactContext.JS);
        
        private String[] suffixes;

        Type(String... suffixes){
            this.suffixes = suffixes;
        }

        public String[] getSuffixes() {
            return suffixes;
        }
    }
    
    public ModuleQuery(String name, Type type){
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
