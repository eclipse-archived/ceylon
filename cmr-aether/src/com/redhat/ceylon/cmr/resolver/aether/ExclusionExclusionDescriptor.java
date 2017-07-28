package com.redhat.ceylon.cmr.resolver.aether;

import com.redhat.ceylon.aether.apache.maven.model.Exclusion;

public class ExclusionExclusionDescriptor implements ExclusionDescriptor {

    private Exclusion x;

    ExclusionExclusionDescriptor(Exclusion x) {
        this.x = x;
    }

    @Override
    public String getGroupId() {
        return x.getGroupId();
    }

    @Override
    public String getArtifactId() {
        return x.getArtifactId();
    }

}
