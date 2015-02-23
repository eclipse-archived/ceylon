package com.redhat.ceylon.cmr.api;

import com.sun.org.apache.bcel.internal.util.Objects;

@SuppressWarnings("serial")
public class MavenArtifactContext extends ArtifactContext {

    private String classifier;
    private String packaging;

    public MavenArtifactContext(String groupId, String artifactId, String version, String packaging, String classifier) {
        super(groupId+":"+artifactId, version);
        this.classifier = classifier;
        this.packaging = packaging;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
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
            if(obj instanceof MavenArtifactContext == false)
                return false;
            MavenArtifactContext other = (MavenArtifactContext) obj;
            return Objects.equals(classifier, other.classifier)
                    && Objects.equals(packaging, other.packaging);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + (classifier != null ? classifier.hashCode() : 0);
        hash = 37 * hash + (packaging != null ? packaging.hashCode() : 0);
        return hash;
    }
}
