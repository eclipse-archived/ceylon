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
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.ModuleSpec;

@Summary("Copies modules from one module repository to another")
@Description("Copies a module or a set of modules from one repository " +
		"to another. If set for recursive copying it will also copy " +
		"all the module's dependencies and their dependencies until the " +
		"entire module tree has been copied.")
public class CeylonCopyTool extends RepoUsingTool {
    
    private List<ModuleSpec> modules;
    private String user;
    private String out;
    private String pass;
    private boolean recursive;
    
    private RepositoryManager orm;
    
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

    @OptionArgument(argumentName="dir-or-url")
    @Description("Specifies the module repository (which must be publishable) " +
            "into which the jar file should be imported. " +
            "(default: `./modules`)")
    public void setOut(String out) {
        this.out = out;
    }
    
    @OptionArgument(argumentName="name")
    @Description("Sets the user name for use with an authenticated output repository.")
    public void setUser(String user) {
        this.user = user;
    }

    @OptionArgument(argumentName="secret")
    @Description("Sets the password for use with an authenticated output repository.")
    public void setPass(String pass) {
        this.pass = pass;
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
    
    protected synchronized RepositoryManager getOutputRepositoryManager() {
        if (orm == null) {
            orm = CeylonUtils.repoManager()
                    .cwd(cwd)
                    .outRepo(out)
//                    .logger(log)
                    .user(user)
                    .password(pass)
                    .offline(offline)
                    .buildOutputManager();
        }
        return orm;
    }
    
    @Override
    public void run() throws Exception {
        setSystemProperties();
        // If all are correct we unpack them
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
                errorMsg("module.not.found", module, getRepositoryManager().getRepositoriesDisplayString());
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
