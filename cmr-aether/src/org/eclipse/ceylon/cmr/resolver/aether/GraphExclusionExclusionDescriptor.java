package org.eclipse.ceylon.cmr.resolver.aether;

import org.eclipse.ceylon.aether.eclipse.aether.graph.Exclusion;

public class GraphExclusionExclusionDescriptor implements ExclusionDescriptor {

    private Exclusion x;

    GraphExclusionExclusionDescriptor(Exclusion x) {
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
