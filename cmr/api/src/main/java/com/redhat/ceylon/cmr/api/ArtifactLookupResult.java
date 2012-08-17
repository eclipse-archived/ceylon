package com.redhat.ceylon.cmr.api;

public class ArtifactLookupResult implements Comparable<ArtifactLookupResult> {
    private String name;

    public ArtifactLookupResult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ArtifactLookupResult other) {
        return name.compareTo(other.name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(obj instanceof ArtifactLookupResult){
            return name.equals(((ArtifactLookupResult)obj).name);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int code = 31;
        code = 37 * code + ArtifactLookupResult.class.getName().hashCode();
        code = 37 * code + name.hashCode();
        return code;
    }
}
