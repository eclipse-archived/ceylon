package com.redhat.ceylon.tools.classpath;

import java.io.File;
import java.util.List;
import java.util.SortedSet;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.tools.moduleloading.ModuleLoadingTool;

@Summary("Prints a classpath suitable for passing to Java tools to run a given Ceylon module")
@Description("Will print a classpath for a given Ceylon module, suitable for use with Java tools to " +
        "run a given Ceylon module outside of the regular JBoss Modules container used in `ceylon run`.")
public class CeylonClasspathTool extends ModuleLoadingTool {

    private List<ModuleSpec> modules;
    private boolean force;

    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }

    @Option(longName="force")
    @Description("Force generation of classpath with multiple versions of the same module.")
    public void setForce(boolean force) {
        this.force = force;
    }
    
    @Override
    public void run() throws Exception {
        // we do depend on having a Main
        loadModule("com.redhat.ceylon.java.main", Versions.CEYLON_VERSION_NUMBER);

        for (ModuleSpec module : modules) {
            String moduleName = module.getName();
            String version = checkModuleVersionsOrShowSuggestions(
                    getRepositoryManager(upgradeDist),
                    moduleName,
                    module.isVersioned() ? module.getVersion() : null,
                    ModuleQuery.Type.JVM,
                    Versions.JVM_BINARY_MAJOR_VERSION,
                    Versions.JVM_BINARY_MINOR_VERSION,
                    null, null, // JS binary but don't care since JVM
                    null);
            if(version == null)
                return;
            loadModule(moduleName, version);
            if(!force)
                errorOnConflictingModule(moduleName, version);
        }
        
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
            if(!versions.isEmpty() && entry.version() != null && !entry.version().equals(versions.last()))
                continue;
            if(once)
                once = false;
            else
                append(File.pathSeparator);
            append(file.getAbsolutePath());
        }
        flush();
    }
}
