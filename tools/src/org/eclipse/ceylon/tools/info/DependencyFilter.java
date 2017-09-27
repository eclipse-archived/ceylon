package org.eclipse.ceylon.tools.info;

import org.eclipse.ceylon.model.cmr.ArtifactResult;

public interface DependencyFilter {
    boolean output(ArtifactResult dep);
    boolean outputDependencies(ArtifactResult dep, int depth);
}