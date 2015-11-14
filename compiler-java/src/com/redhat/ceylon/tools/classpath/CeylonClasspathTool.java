package com.redhat.ceylon.tools.classpath;

import java.io.File;
import java.util.SortedSet;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.tools.moduleloading.ModuleLoadingTool;

@Summary("Prints a classpath suitable for passing to Java tools to run a given Ceylon module")
@Description("Will print a classpath for a given Ceylon module, suitable for use with Java tools to " +
        "run a given Ceylon module outside of the regular JBoss Modules container used in `ceylon run`.")
public class CeylonClasspathTool extends ModuleLoadingTool {

    private String moduleNameOptVersion;
    private boolean force;

    @Argument(argumentName="module", multiplicity = "1")
    public void setModule(String module) {
        this.moduleNameOptVersion = module;
    }

    @Option(longName="force")
    @Description("Force generation of classpath with multiple versions of the same module.")
    public void setForce(boolean force) {
        this.force = force;
    }

    @Override
    public void run() throws Exception {
        String module = ModuleUtil.moduleName(moduleNameOptVersion);
        String version = checkModuleVersionsOrShowSuggestions(
                getRepositoryManager(),
                module,
                ModuleUtil.moduleVersion(moduleNameOptVersion),
                ModuleQuery.Type.JVM,
                Versions.JVM_BINARY_MAJOR_VERSION,
                Versions.JVM_BINARY_MINOR_VERSION,
                null);
        if(version == null)
            return;
        loadModule(module, version);

        if(!force)
            errorOnConflictingModule(module, version);
        
        boolean once = true;
        for(ArtifactResult entry : this.loadedModules.values()){
            // since we even add missing modules there to avoid seeing them twice, let's skip them now
            if(entry == null)
                continue;
            File file = entry.artifact();
            if(file == null)
                continue;
            // on duplicate, let's only keep the last version
            SortedSet<String> versions = loadedModuleVersions.get(entry.name());
            if(version != null && !versions.isEmpty() && entry.version() != null && !entry.version().equals(versions.last()))
                continue;
            if(once)
                once = false;
            else
                append(File.pathSeparator);
            append(file.getAbsolutePath());
        }
        flush();
    }

    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
    }
}
