package com.redhat.ceylon.tools.copy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils.CeylonRepoManagerBuilder;
import com.redhat.ceylon.cmr.ceylon.ModuleCopycat;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.common.BooleanUtil;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.ModuleSpec;

@Summary("Copies modules from one module repository to another")
@Description("Copies a module or a set of modules from one repository " +
		"to another. If set for recursive copying it will also copy " +
		"all the module's dependencies and their dependencies until the " +
		"entire module tree has been copied.")
@RemainingSections(OutputRepoUsingTool.DOCSECTION_REPOSITORIES)
public class CeylonCopyTool extends OutputRepoUsingTool {
    
    private List<ModuleSpec> modules;
    private boolean withDependencies;
    
    private Boolean jvm;
    private Boolean js;
    private Boolean docs;
    private Boolean src;
    private Boolean scripts;
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

    @Option(longName="with-dependencies", shortName='r')
    @Description("Recursively copy all dependencies")
    public void setWithDependencies(boolean withDependencies) {
        this.withDependencies = withDependencies;
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
    @Description("Include scripts (default: false)")
    public void setScripts(boolean scripts) {
        this.scripts = scripts;
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
    protected boolean needsSystemRepo() {
        return false;
    }

    @Override
    protected boolean doNotReadFromOutputRepo(){
        return true;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
    }
    
    @Override
    public void run() throws Exception {
        Set<String> artifacts = new LinkedHashSet<String>();
        boolean defaults = js == null 
                && jvm == null
                && src == null
                && scripts == null
                && docs == null
                && all == null;
        if (BooleanUtil.isTrue(all)) {
            artifacts.addAll(Arrays.asList(ArtifactContext.allSuffixes()));
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
        if (BooleanUtil.isTrue(scripts)) {
            artifacts.add(ArtifactContext.SCRIPTS_ZIPPED);
        } else if (BooleanUtil.isFalse(scripts)) {
            artifacts.remove(ArtifactContext.SCRIPTS_ZIPPED);
        }
        if (BooleanUtil.isTrue(docs)) {
            artifacts.add(ArtifactContext.DOCS);
        } else if (BooleanUtil.isFalse(docs)) {
            artifacts.remove(ArtifactContext.DOCS);
        }

        // Create the list of ArtifactContexts to copy
        List<ArtifactContext> acs = new ArrayList<ArtifactContext>();
        String[] artifactsArray = new String[artifacts.size()];
        artifacts.toArray(artifactsArray);
        for (ModuleSpec module : modules) {
            if (module != ModuleSpec.DEFAULT_MODULE && !module.isVersioned()) {
                String version = checkModuleVersionsOrShowSuggestions(getRepositoryManager(), module.getName(), null, ModuleQuery.Type.ALL, null, null);
                module = new ModuleSpec(module.getName(), version);
            }
            ArtifactContext ac = new ArtifactContext(module.getName(), module.getVersion(), artifactsArray);
            ac.setIgnoreDependencies(!withDependencies);
            ac.setForceOperation(true);
            acs.add(ac);
        }
        
        // Now do the actual copying
        final boolean logArtifacts = verbose != null && (verbose.contains("all") || verbose.contains("files"));
        ModuleCopycat copier = new ModuleCopycat(getRepositoryManager(), getOutputRepositoryManager(), log, new ModuleCopycat.CopycatFeedback() {
            @Override
            public boolean beforeCopyModule(ArtifactContext ac, int count, int max) throws IOException {
                String module = ModuleUtil.makeModuleName(ac.getName(), ac.getVersion());
                msg("copying.module", module, count+1, max).flush();
                if (logArtifacts) {
                    newline().flush();
                }
                return true;
            }
            @Override
            public void afterCopyModule(ArtifactContext ac, int count, int max, boolean copied) throws IOException {
                if (!logArtifacts) {
                    append(" ").msg((copied) ? "copying.ok" : "copying.skipped").newline().flush();
                }
            }
            @Override
            public boolean beforeCopyArtifact(ArtifactContext ac, File archive, int count, int max) throws IOException {
                if (logArtifacts) {
                    append("    ").msg("copying.artifact", archive.getName(), count+1, max).flush();
                }
                return true;
            }
            @Override
            public void afterCopyArtifact(ArtifactContext ac, File archive, int count, int max, boolean copied) throws IOException {
                if (logArtifacts) {
                    append(" ").msg((copied) ? "copying.ok" : "copying.skipped").newline().flush();
                }
            }
            @Override
            public void notFound(ArtifactContext ac) throws IOException {
                String err = getModuleNotFoundErrorMessage(getRepositoryManager(), ac.getName(), ac.getVersion());
                errorAppend(err);
                errorNewline();
            }
        });
        copier.copyModules(acs);
    }

    @Override
    protected CeylonRepoManagerBuilder createRepositoryManagerBuilder(boolean forInput) {
        return createRepositoryManagerBuilderNoOut(forInput);
    }

}
