package com.redhat.ceylon.tools.info;

import com.redhat.ceylon.model.cmr.ArtifactResult;

public interface DependencyFilter {
    boolean output(ArtifactResult dep);
    boolean outputDependencies(ArtifactResult dep, int depth);
}