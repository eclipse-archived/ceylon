package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.util.List;

/**
 * @deprecated Replaced by {@link #DocTool}
 * @author tom
 */
@Deprecated
public class CeylonDocTool extends DocTool {

    public CeylonDocTool(List<File> sourceFolders, List<String> repositories, List<String> moduleSpecs,
            boolean haltOnError) {
        super();
        setSourceFolders(sourceFolders);
        setRepositories(repositories);
        setModuleSpecs(moduleSpecs);
        setHaltOnError(haltOnError);
        init();
    }   
}
