package com.redhat.ceylon.tools.classpath;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ImportType;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.compiler.typechecker.model.Module;

@Summary("Prints a classpath suitable for passing to Java tools to run a given Ceylon module")
@Description("Will print a classpath for a given Ceylon module, suitable for use with Java tools to " +
        "run a given Ceylon module outside of the regular JBoss Modules container used in `ceylon run`.")
public class CeylonClasspathTool extends RepoUsingTool {

    private String moduleNameOptVersion;

    private Map<String, ArtifactResult> loadedModules = new HashMap<String, ArtifactResult>();
    
    public CeylonClasspathTool() {
        super(CeylonClasspathMessages.RESOURCE_BUNDLE);
    }
    
    @Argument(argumentName="module", multiplicity = "1")
    public void setModule(String module) {
        this.moduleNameOptVersion = module;
    }
    
    @Override
    public void run() throws Exception {
        setSystemProperties();
        
        String moduleName = ModuleUtil.moduleName(moduleNameOptVersion);
        String moduleVersion = checkModuleVersionsOrShowSuggestions(
                getRepositoryManager(),
                moduleName,
                ModuleUtil.moduleVersion(moduleNameOptVersion),
                ModuleQuery.Type.JVM,
                Versions.JVM_BINARY_MAJOR_VERSION,
                Versions.JVM_BINARY_MINOR_VERSION);
        if (moduleVersion == null) {
            return;
        }
        
        loadModule(Module.LANGUAGE_MODULE_NAME, Versions.CEYLON_VERSION_NUMBER, false);
        loadModule("com.redhat.ceylon.compiler.java", Versions.CEYLON_VERSION_NUMBER, false);
        loadModule("com.redhat.ceylon.common", Versions.CEYLON_VERSION_NUMBER, false);
        loadModule("com.redhat.ceylon.module-resolver", Versions.CEYLON_VERSION_NUMBER, false);
        loadModule("com.redhat.ceylon.typechecker", Versions.CEYLON_VERSION_NUMBER, false);
        loadModule(moduleName, moduleVersion, false);
        
        boolean once = true;
        for(ArtifactResult entry : loadedModules.values()){
            if(once)
                once = false;
            else
                append(File.pathSeparator);
            append(entry.artifact().getAbsolutePath());
        }
        flush();
    }

    private void loadModule(String name, String version, boolean optional) throws IOException {
        String key = name + "/" + version;
        if(loadedModules.containsKey(key))
            return;
        RepositoryManager repositoryManager = getRepositoryManager();
        ArtifactContext artifactContext = new ArtifactContext(name, version, ArtifactContext.CAR, ArtifactContext.JAR);
        ArtifactResult result = repositoryManager.getArtifactResult(artifactContext);
        if(!optional
                && (result == null || result.artifact() == null || !result.artifact().exists())){
            String err = getModuleNotFoundErrorMessage(repositoryManager, name, version);
            errorAppend(err);
            errorNewline();
            return;
        }
        // save even missing optional modules as nulls to not re-resolve them
        loadedModules.put(key, result);
        if(result != null){
            for(ArtifactResult dep : result.dependencies()){
                loadModule(dep.name(), dep.version(), dep.importType() == ImportType.OPTIONAL);
            }
        }
    }

    @Override
    public void initialize() throws Exception {
    }
}
