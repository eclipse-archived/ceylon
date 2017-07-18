package com.redhat.ceylon.cmr.api;

import java.util.Objects;

public class MavenArtifactContext extends ArtifactContext {
    private static final long serialVersionUID = -7584989942190722636L;
    
    private String classifier;
    private String packaging;

    public static final String NAMESPACE = "maven";
    
    //copy-pasted from MavenUtils
    private static String moduleName(String groupId, String artifactId, String classifier) {
        return classifier==null || classifier.isEmpty() ? 
                groupId+":"+artifactId : 
                groupId+":"+artifactId+":"+classifier;
    }
    
    public MavenArtifactContext(String groupId, String artifactId, String classifier, String version, String packaging) {
        super(NAMESPACE, moduleName(groupId, artifactId, classifier), version);
        this.classifier = classifier;
        this.packaging = packaging;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            if(obj == this)
                return true;
            if(obj instanceof MavenArtifactContext == false){
                // make ourselves pass equal to ArtifactContext if we add nothing to it
                if(classifier == null && packaging == null)
                    return true;
                // we have extra data
                return false;
            }
            MavenArtifactContext other = (MavenArtifactContext) obj;
            return Objects.equals(classifier, other.classifier)
                && Objects.equals(packaging, other.packaging);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        // make ourselves pass equal to ArtifactContext if we add nothing to it
        if(classifier == null && packaging == null)
            return hash;
        hash = 37 * hash + (classifier != null ? classifier.hashCode() : 0);
        hash = 37 * hash + (packaging != null ? packaging.hashCode() : 0);
        return hash;
    }
}
