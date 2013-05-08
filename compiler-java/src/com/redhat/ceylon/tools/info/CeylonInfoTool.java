package com.redhat.ceylon.tools.info;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils.CeylonRepoManagerBuilder;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.tools.ModuleSpec;

@Summary("Prints information about a module")
@Description("Prints information about the contents of a module archive, " +
		"its description, its licence, and its dependencies")
public class CeylonInfoTool implements Tool {

    // TODO filter by backend (.car only, js only)
    // TODO machine readable output (JSON, or dot, or ceylon, gephi)
    
    private static final int INFINITE_DEPTH = -1; 
    
    private List<String> repo;
    private String sysrep;

    private List<ModuleSpec> modules;
    
    private Appendable out = System.out;
    private Appendable error = System.err;
    
    private DependencyFilter depFilter;

    private int depth = 1;
    private boolean offline;
    
    private RepositoryManager rm;
    
    public void setOut(Appendable out) {
        this.out = out;
    }
    
    @Description("A module repository containing source archives " +
            "(default: `./modules`, http://modules.ceylon-lang.org")
    @OptionArgument(argumentName="repo")
    public void setRepo(List<String> repo) {
        this.repo = repo;
    }
    
    @OptionArgument(longName="sysrep", argumentName="url")
    @Description("The system repository containing essential modules. " +
            "(default: `$CEYLON_HOME/repo`)")
    public void setSystemRepository(String systemRepo) {
        this.sysrep = systemRepo;
    }

    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules, ModuleSpec.Option.VERSION_REQUIRED));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }
    
    @Description("The depth of the dependency tree to show, or `all` for the full tree. " +
    		"(Allowed values: any positive integer or `all`, default: `1`)")
    @OptionArgument(argumentName="depth")
    public void setDependencyDepth(String depth) {
        if ("all".equals(depth)) {
            setDependencyDepth(INFINITE_DEPTH);
        } else {
            setDependencyDepth(Integer.parseInt(depth));
        }
    }
    
    public void setDependencyDepth(int depth) {
        if (!(depth == INFINITE_DEPTH || depth >= 0)) {
            throw new IllegalArgumentException(CeylonInfoMessages.msg("illegal.depth"));
        }
        this.depth = depth;
    }
    
    @Option(longName="offline")
    @Description("Enables offline mode that will prevent the module loader from connecting to remote repositories.")
    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    @PostConstruct
    public void init() {
        if (depFilter == null) {
            depFilter = new DependencyFilter() {
                @Override
                public boolean outputDependencies(ArtifactResult dep, int depth) {
                    return CeylonInfoTool.this.depth == INFINITE_DEPTH 
                            || depth < CeylonInfoTool.this.depth;
                }
                @Override
                public boolean output(ArtifactResult dep) {
                    return true;
                }
            };
        }
    
        CeylonRepoManagerBuilder rmb = CeylonUtils.repoManager()
                .systemRepo(sysrep)
                .userRepos(repo)
                .offline(offline);
            
        rm = rmb.buildManager();   
    }
    
    @Override
    public void run() throws Exception {
        for (ModuleSpec module : modules) {
            
            ArtifactContext context = new ArtifactContext(module.getName(), module.getVersion(), ArtifactContext.CAR);
            ArtifactResult result = rm.getArtifactResult(context);
            if (result == null) {
                context.setSuffix(ArtifactContext.JAR);
                result = rm.getArtifactResult(context);
            }
            if (result == null) {
                errorMsg("module.not.found", module, rm.getRepositoriesDisplayString());
                continue;
            }
            outputModule(result);
        }
    }

    private void outputModule(ArtifactResult result) throws IOException {
        msg("module.name").append(result.name()).newline();
        msg("module.version").append(result.version()).newline();
        
        // XXX how can I get the licence and description without executing any code from the module?
        // In particular I want to avoid running any `<cinit>` on the module descriptor
        //msg("module.description").append("TODO").newline();
        //msg("module.license").append("TODO").newline();
        // XXX print url as extracted from module descriptor
        // XXX info about artifacts (e.g. src, car, js, etc) and/or supports JVM/JS?
        
        msg("module.dependencies", (depth == INFINITE_DEPTH ? "∞" : String.valueOf(depth))).newline();
        
        int depth = 0;
        recurseDependencies(result, depth);
    }

    private void dependency(ArtifactResult dep, final int depth)
            throws IOException {
        if (depFilter.output(dep)) {
            for (int ii = 0; ii < depth; ii++) {
                append("  ");
            }
            append(dep.name()).append("/").append(dep.version());
            switch (dep.importType()) {
            case UNDEFINED:
                break;
            case EXPORT:
                append(" (shared)");
                break;
            case OPTIONAL:
                append(" (optional)");
                break;
            }
            newline();
        }
        recurseDependencies(dep, depth);      
    }

    private void recurseDependencies(ArtifactResult dep, final int depth)
            throws IOException {
        if (depFilter.outputDependencies(dep, depth)) {
            List<ArtifactResult> dependencies;
            try {
                dependencies = dep.dependencies();
            } catch (IllegalArgumentException e) {
                errorMsg("module.not.found", dep.name()+"/"+dep.version(), rm.getRepositoriesDisplayString());
                return;
            }
            for (ArtifactResult dep2 : dependencies) {
                dependency(dep2, depth+1);
            }
        }
    }
    
    private CeylonInfoTool msg(String msgKey, Object...msgArgs) throws IOException {
        out.append(CeylonInfoMessages.msg(msgKey, msgArgs));
        return this;
    }
    
    private CeylonInfoTool errorMsg(String msgKey, Object...msgArgs) throws IOException {
        error.append(CeylonInfoMessages.msg(msgKey, msgArgs));
        error.append(System.lineSeparator());
        return this;
    }
    
    private CeylonInfoTool append(Object s) throws IOException {
        out.append(String.valueOf(s));
        return this;
    }
    
    private CeylonInfoTool newline() throws IOException {
        out.append(System.lineSeparator());
        return this;
    }

}
