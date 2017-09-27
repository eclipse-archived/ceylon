package org.eclipse.ceylon.model.loader;

import org.eclipse.ceylon.model.cmr.ArtifactResult;

public interface StaticMetamodelLoader {
    public void loadModule(String name, String version, ArtifactResult artifact);
}
