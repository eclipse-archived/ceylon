package com.redhat.ceylon.tools.copy;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.common.BooleanUtil;
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
    private Boolean jvm;
    private Boolean js;
    private Boolean docs;
    private Boolean src;
    private Boolean all;
    
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
    @Description("Include modules compiled for the JVM (.car) (default: true)")
    public void setJvm(boolean jvm) {
        this.jvm = jvm;
    }

    @Option
    @Description("Include modules compiled for the JSVM (.js) (default: true)")
    public void setJs(boolean js) {
        this.js = js;
    }

    @Option
    @Description("Include documentation (default: false)")
    public void setDocs(boolean docs) {
        this.docs = docs;
    }

    @Option
    @Description("Include sources (default: false)")
    public void setSrc(boolean src) {
        this.src = src;
    }

    @Option
    @Description("Include everything (jvm,js,docs,src) (default: false)")
    public void setAll(boolean all) {
        this.all = all;
    }

    @Option(shortName='d')
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
        // unnecessary roundtrips to external servers
        Set<String> artifacts = new LinkedHashSet<String>();
        boolean defaults = js == null 
                && jvm == null
                && src == null
                && docs == null
                && all == null;
        if (BooleanUtil.isTrue(all)) {
            artifacts.addAll(ArtifactContext.allSuffixes());
        }
        if (BooleanUtil.isTrue(js) || defaults) {
            artifacts.add(ArtifactContext.JS);
            artifacts.add(ArtifactContext.JS_MODEL);
            artifacts.add(ArtifactContext.RESOURCES);
        } else if (BooleanUtil.isFalse(js)) {
            artifacts.remove(ArtifactContext.JS);
            artifacts.remove(ArtifactContext.JS_MODEL);
            artifacts.remove(ArtifactContext.RESOURCES);
        }
        if (BooleanUtil.isTrue(jvm) || defaults) {
            // put the CAR first since its presence will shortcut the other three
            artifacts.add(ArtifactContext.CAR);
            artifacts.add(ArtifactContext.JAR);
            artifacts.add(ArtifactContext.MODULE_PROPERTIES);
            artifacts.add(ArtifactContext.MODULE_XML);
        } else if (BooleanUtil.isFalse(jvm)) {
            artifacts.remove(ArtifactContext.CAR);
            artifacts.remove(ArtifactContext.JAR);
            artifacts.remove(ArtifactContext.MODULE_PROPERTIES);
            artifacts.remove(ArtifactContext.MODULE_XML);
        }
        if (BooleanUtil.isTrue(src)) {
            artifacts.add(ArtifactContext.SRC);
        } else if (BooleanUtil.isFalse(src)) {
            artifacts.remove(ArtifactContext.SRC);
        }
        if (BooleanUtil.isTrue(docs)) {
            artifacts.add(ArtifactContext.DOCS_ZIPPED);
            artifacts.add(ArtifactContext.DOCS);
        } else if (BooleanUtil.isFalse(docs)) {
            artifacts.remove(ArtifactContext.DOCS_ZIPPED);
            artifacts.remove(ArtifactContext.DOCS);
        }
        for (ModuleSpec module : modules) {
            if (module != ModuleSpec.DEFAULT_MODULE && !module.isVersioned()) {
                String version = checkModuleVersionsOrShowSuggestions(getRepositoryManager(), module.getName(), null, ModuleQuery.Type.ALL, null, null);
                module = new ModuleSpec(module.getName(), version);
            }
            copyModule(module, artifacts);
        }
    }

    private void copyModule(ModuleSpec module, Set<String> artifacts) throws IOException {
        if (!copiedModules.add(module)) {
            return;
        }
        if (!JDKUtils.isJDKModule(module.getName()) && !JDKUtils.isOracleJDKModule(module.getName())) {
            Collection<ModuleVersionDetails> versions = getModuleVersions(module.getName(), module.getVersion(), ModuleQuery.Type.ALL, null, null);
            if (!versions.isEmpty()) {
                ModuleVersionDetails ver = versions.iterator().next();
                msg("copying.module", module).newline().flush();
                ArtifactResult result = null;
                Set<String> suffixes = new LinkedHashSet<String>(artifacts);
                while (!suffixes.isEmpty()) {
                    // Try to get one of the artifacts
                    String[] sfx = new String[suffixes.size()];
                    sfx = suffixes.toArray(sfx);
                    ArtifactContext ac;
                    if (result == null) {
                        ac = new ArtifactContext(module.getName(), module.getVersion(), sfx);
                    } else {
                        // We re-use the previous result so we can efficiently
                        // retrieve further artifacts from the same module
                        ac = result.getSiblingArtifact(sfx);
                    }
                    ac.setThrowErrorIfMissing(false);
                    result = getRepositoryManager().getArtifactResult(ac);
                    
                    if (result != null) {
                        // Set the proper suffix for the copy operation
                        String suffix = ArtifactContext.getSuffixFromFilename(result.artifact().getName());
                        ac.setSuffixes(suffix);
                        
                        // Perform the actual copying
                        copyArtifact(ac, result.artifact());
                        
                        // make sure we don't try to get the same artifact twice
                        suffixes.remove(suffix);
                        
                        // if we found a car we can skip the jar and module descriptors
                        if (suffix.equals(ArtifactContext.CAR)) {
                            suffixes.remove(ArtifactContext.JAR);
                            suffixes.remove(ArtifactContext.MODULE_PROPERTIES);
                            suffixes.remove(ArtifactContext.MODULE_XML);
                        }
                    } else {
                        // We didn't find anything (this time),
                        // we can stop trying more artifact types
                        break;
                    }
                }
                if (recursive) {
                    for (ModuleInfo dep : ver.getDependencies()) {
                        ModuleSpec depModule = new ModuleSpec(dep.getName(), dep.getVersion());
                        copyModule(depModule, artifacts);
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
            append("    ").msg("copying.artifact", archive.getName()).newline().flush();
        }
        getOutputRepositoryManager().putArtifact(ac, archive);
        // SHA1 it if required
        if(!ArtifactContext.isDirectoryName(ac.getSingleSuffix())) {
            signArtifact(ac, archive);
        }
    }

}
