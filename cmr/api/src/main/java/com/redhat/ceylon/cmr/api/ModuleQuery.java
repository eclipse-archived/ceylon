package com.redhat.ceylon.cmr.api;

public class ModuleQuery {
    private String name;
    private Type type;
    private long start;
    private long count;

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
        this.name = name.toLowerCase();
        this.type = type;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
