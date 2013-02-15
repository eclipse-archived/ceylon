package com.redhat.ceylon.tools.info;

import com.redhat.ceylon.cmr.api.ArtifactResult;

public interface DependencyFilter {
    boolean output(ArtifactResult dep);
    boolean outputDependencies(ArtifactResult dep, int depth);
}