package com.redhat.ceylon.model.loader;

import com.redhat.ceylon.model.cmr.ArtifactResult;

public interface StaticMetamodelLoader {
    public void loadModule(String name, String version, ArtifactResult artifact);
}
