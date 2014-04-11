package com.redhat.ceylon.tools.copy;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.ModuleSpec;

@Summary("Copies modules from one module repository to another")
@Description("Copies a module or a set of modules from one repository " +
		"to another. If set for recursive copying it will also copy " +
		"all the module's dependencies and their dependencies until the " +
		"entire module tree has been copied.")
@RemainingSections(OutputRepoUsingTool.DOCSECTION_REPOSITORIES)
public class CeylonCopyTool extends OutputRepoUsingTool {
    
    private List<ModuleSpec> modules;
    private boolean recursive;
    
    private HashSet<ModuleSpec> copiedModules = new HashSet<ModuleSpec>();
    
    public CeylonCopyTool() {
        super(CeylonCopyMessages.RESOURCE_BUNDLE);
    }
    
    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }

    @Option
    @Description("Recursively copy dependencies")
    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Produce verbose output. " +
            "If no `flags` are given then be verbose about everything, " +
            "otherwise just be verbose about the flags which are present. " +
            "Allowed flags include: `all`, `loader`, `files`.")
    public void setVerbose(String verbose) {
        this.verbose = verbose;
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public void run() throws Exception {
        setSystemProperties();
        // FIXME: copying is currently very inefficient!
        // All possible suffix types are tried which will result in numerous
        // unnecessary roundtrips to external servers, also, no de-duplication
        // is done so if a module is encountered several times it will be
        // copied the same number of times (only one copy will exist but it's slow)
        for (ModuleSpec module : modules) {
            if (module != ModuleSpec.DEFAULT_MODULE && !module.isVersioned()) {
                String version = checkModuleVersionsOrShowSuggestions(getRepositoryManager(), module.getName(), null, ModuleQuery.Type.ALL, null, null);
                module = new ModuleSpec(module.getName(), version);
            }
            copyModule(module);
        }
    }

    private void copyModule(ModuleSpec module) throws IOException {
        if (copiedModules.contains(module)) {
            return;
        }
        copiedModules.add(module);
        if (!JDKUtils.isJDKModule(module.getName()) && !JDKUtils.isOracleJDKModule(module.getName())) {
            Collection<ModuleVersionDetails> versions = getModuleVersions(module.getName(), module.getVersion(), ModuleQuery.Type.ALL, null, null);
            if (!versions.isEmpty()) {
                ModuleVersionDetails ver = versions.iterator().next();
                msg("copying.module", module).newline();
                for (String suffix : ArtifactContext.userSuffixes) {
                    ArtifactContext ac = new ArtifactContext(module.getName(), module.getVersion(), suffix);
                    ArtifactResult srcArchive = getRepositoryManager().getArtifactResult(ac);
                    if (srcArchive != null) {
                        copyArtifact(ac, srcArchive.artifact());
                    }
                }
                if (recursive) {
                    for (ModuleInfo dep : ver.getDependencies()) {
                        ModuleSpec depModule = new ModuleSpec(dep.getName(), dep.getVersion());
                        copyModule(depModule);
                    }
                }
            } else {
                String err = getModuleNotFoundErrorMessage(getRepositoryManager(), module.getName(), module.getVersion());
                errorAppend(err);
                errorNewline();
            }
        }
    }
    
    private void copyArtifact(ArtifactContext ac, File archive) throws RepositoryException, IOException {
        if (verbose != null && (verbose.contains("all") || verbose.contains("files"))) {
            msg("copying.artifact", archive).newline();
        }
        getOutputRepositoryManager().putArtifact(ac, archive);
    }

}
