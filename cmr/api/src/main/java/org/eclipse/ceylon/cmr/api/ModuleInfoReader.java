package org.eclipse.ceylon.cmr.api;

import java.io.File;

import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.cmr.api.Overrides;

public interface ModuleInfoReader {

    public boolean matchesModuleInfo(String moduleName, String version, File moduleArchive, String query, Overrides overrides);

    public ModuleVersionDetails readModuleInfo(String moduleName, String version, File moduleArchive, boolean includeMembers, Overrides overrides);

    public int[] getBinaryVersions(String moduleName, String version, File moduleArchive);

}
