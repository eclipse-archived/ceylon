package com.redhat.ceylon.tools.moduleloading;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.VersionComparator;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.Messages;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ImportType;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.cmr.JDKUtils.JDK;
import com.redhat.ceylon.model.typechecker.model.Module;

public abstract class ModuleLoadingTool extends RepoUsingTool {

    protected Map<String, ArtifactResult> loadedModules = new HashMap<>();
    protected Map<String, SortedSet<String>> loadedModuleVersions = new HashMap<>();
    protected boolean upgradeDist = true;

	public ModuleLoadingTool() {
		super(ModuleLoadingMessages.RESOURCE_BUNDLE);
	}
    
    @Option
    @Description("Downgrade which were compiled with a more recent "
            + "version of the distribution to the version of that module "
            + "present in this distribution (" + Versions.CEYLON_VERSION_NUMBER + "). "
            + "This might fail with a linker error at runtime. For example "
            + "if the module depended on an API present in the more "
            + "recent version, but absent from " + Versions.CEYLON_VERSION_NUMBER +". "
                    + "Allowed arguments are upgrade, downgrade or abort. Default: upgrade")
    public void setDowngradeDist(boolean downgradeDist) {
        this.upgradeDist = !downgradeDist;
    }
	
	protected String moduleVersion(String moduleNameOptVersion) throws IOException {
		return checkModuleVersionsOrShowSuggestions(
				getRepositoryManager(),
				ModuleUtil.moduleName(moduleNameOptVersion),
				ModuleUtil.moduleVersion(moduleNameOptVersion),
				ModuleQuery.Type.JVM,
				Versions.JVM_BINARY_MAJOR_VERSION,
				Versions.JVM_BINARY_MINOR_VERSION,
				// JS binary but don't care since JVM
				null, null);

	}
	
	protected boolean loadModule(String moduleName, String moduleVersion) throws IOException {
		return loadModule(moduleName, moduleVersion, false);
	}
	
	protected boolean loadModule(String moduleName, String moduleVersion, boolean optional) throws IOException {
		boolean success = false;
		if (moduleVersion != null) {
			success = true;
			success &= internalLoadModule(Module.LANGUAGE_MODULE_NAME, Versions.CEYLON_VERSION_NUMBER, false);
			success &= internalLoadModule("com.redhat.ceylon.common", Versions.CEYLON_VERSION_NUMBER, false);
            success &= internalLoadModule("com.redhat.ceylon.model", Versions.CEYLON_VERSION_NUMBER, false);
			success &= internalLoadModule("com.redhat.ceylon.module-resolver", Versions.CEYLON_VERSION_NUMBER, false);
			success &= internalLoadModule(moduleName, moduleVersion, false);
		}
		
		return success;
	}

	protected boolean shouldExclude(String moduleName, String version) {
		if(JDKUtils.jdk.providesVersion(JDK.JDK9.version)){
			moduleName = JDKUtils.getJava9ModuleName(moduleName, version);
		}
		return JDKUtils.isJDKModule(moduleName) || 
				JDKUtils.isOracleJDKModule(moduleName);
	}

	private boolean internalLoadModule(String name, String version, boolean optional) throws IOException {
        String key = name + "/" + version;
        if(loadedModules.containsKey(key))
            return true;
        if(shouldExclude(name, version)) {
            // let's not check the version and assume it's provided
            // treat it as a missing optional for the purpose of classpath
            loadedModules.put(key, null);
            return true;
        }
        // remember which version we loaded
        SortedSet<String> loadedVersions = loadedModuleVersions.get(name);
        if(loadedVersions == null){
            loadedVersions = new TreeSet<>(VersionComparator.INSTANCE);
            loadedModuleVersions.put(name, loadedVersions);
        }
        loadedVersions.add(version);
        
        RepositoryManager repositoryManager = getRepositoryManager(upgradeDist);
        ArtifactContext artifactContext = new ArtifactContext(name, version, ArtifactContext.CAR, ArtifactContext.JAR);
        ArtifactResult result = repositoryManager.getArtifactResult(artifactContext);
        if(!optional
                && (result == null || result.artifact() == null || !result.artifact().exists())){
            String err = getModuleNotFoundErrorMessage(repositoryManager, name, version);
            errorAppend(err);
            errorNewline();
            return false;
        }
        // save even missing optional modules as nulls to not re-resolve them
        loadedModules.put(key, result);
        if(result != null){
            for(ArtifactResult dep : result.dependencies()){
                internalLoadModule(dep.name(), dep.version(), dep.importType() == ImportType.OPTIONAL);
            }
        }
        
        return true;
    }
	
	protected void errorOnConflictingModule(String module, String version) throws IOException{
	    boolean duplicate = false;
	    for(Map.Entry<String, SortedSet<String>> entry : loadedModuleVersions.entrySet()){
	        if(entry.getValue().size() > 1){
	            duplicate = true;
	            printDuplicateModuleErrorMessage(entry.getKey(), entry.getValue());
	        }
	    }
	    if(duplicate)
	        throw new ToolUsageError(Messages.msg(bundle, "module.conflict.error", module, version));
	}

    private void printDuplicateModuleErrorMessage(String name, SortedSet<String> versions) throws IOException {
        StringBuilder err = new StringBuilder();
        boolean first = true;
        for(String version : versions){
            if(first)
                first = false;
            else
                err.append(", ");
            err.append(version);
        }
        errorMsg("module.duplicate.error", name, err, versions.last());
    }
}
