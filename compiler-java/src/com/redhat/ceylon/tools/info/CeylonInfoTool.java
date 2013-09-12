package com.redhat.ceylon.tools.info;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.tools.ModuleSpec;

@Summary("Prints information about a module")
@Description("Prints information about the contents of a module archive, " +
		"its description, its licence, and its dependencies")
public class CeylonInfoTool extends RepoUsingTool {

    // TODO filter by backend (.car only, js only)
    // TODO machine readable output (JSON, or dot, or ceylon, gephi)
    
    private static final int INFINITE_DEPTH = -1; 
    
    private List<ModuleSpec> modules;
    
    private int depth = 1;
    
    public CeylonInfoTool() {
        super(CeylonInfoMessages.RESOURCE_BUNDLE);
    }
    
    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
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
    
    @Override
    public void run() throws Exception {
        for (ModuleSpec module : modules) {
            // TODO Figure out how to use Type.ALL instead of Type.JVM
            Collection<ModuleVersionDetails> versions = getModuleVersions(module.getName(), module.getVersion(), ModuleQuery.Type.ALL, null);
            if (versions.isEmpty()) {
                errorMsg("module.not.found", module, getRepositoryManager().getRepositoriesDisplayString());
                continue;
            }
            outputModuleVersions(module, versions);
        }
    }

    private void outputModuleVersions(ModuleSpec module, Collection<ModuleVersionDetails> versions) throws IOException {
        msg("module.name").append(module.getName()).newline();
        boolean first = true;
        for (ModuleVersionDetails version : versions) {
            if (!first) {
                newline();
            }
            outputVersion(version);
            first = false;
        }
    }

    private void outputVersion(ModuleVersionDetails version) throws IOException {
        msg("module.version").append(version.getVersion()).newline();
        outputArtifacts(version.getArtifactTypes());
        msg("module.available").append((version.isRemote() ? "No" : "Yes")).newline();
        if (version.getOrigin() != null) {
            msg("module.origin").append(version.getOrigin()).newline();
        }
        if (version.getDoc() != null) {
            msg("module.description").append(version.getDoc()).newline();
        }
        if (version.getLicense() != null) {
            msg("module.license").append(version.getLicense()).newline();
        }
        if (!version.getDependencies().isEmpty()) {
            msg("module.dependencies", (depth == INFINITE_DEPTH ? "∞" : String.valueOf(depth))).newline();
            recurseDependencies(version, 0);
        }
    }

    private RepoUsingTool outputArtifacts(Set<ModuleVersionArtifact> types) throws IOException {
        if(!types.isEmpty()) {
            msg("module.artifacts");
            boolean first = true;
            for (ModuleVersionArtifact type : types) {
                if (!first) {
                    append(", ");
                }
                String suffix = type.getSuffix();
                if (suffix.equalsIgnoreCase(".car")) {
                    append("JVM (#");
                    if (type.getMajorBinaryVersion() != null) {
                        append(type.getMajorBinaryVersion());
                    }
                    if (type.getMinorBinaryVersion() != null) {
                        append(".");
                        append(type.getMinorBinaryVersion());
                    }
                    if (type.getMajorBinaryVersion() != Versions.JVM_BINARY_MAJOR_VERSION) {
                        append(" *incompatible*");
                    }
                    append(")");
                } else if (suffix.equalsIgnoreCase(".jar")) {
                    append("JVM (legacy)");
                } else if (suffix.equalsIgnoreCase(".js")) {
                    append("JavaScript");
                } else if (suffix.equalsIgnoreCase(".src")) {
                    append("Sources");
                } else if (suffix.startsWith(".")) {
                    append(suffix.substring(1).toUpperCase());
                } else {
                    // We might need to add some special cases
                    // for these in the above list
                    append(type);
                }
                first = false;
            }
            newline();
        }
        return this;
    }
    
    private void recurseDependencies(ModuleVersionDetails version, final int depth) throws IOException {
        for (ModuleInfo dep : version.getDependencies()) {
            dependency(dep, depth+1);
        }
    }
    
    private void dependency(ModuleInfo dep, final int depth) throws IOException {
        for (int ii = 0; ii < depth; ii++) {
            append("  ");
        }
        append(dep);
        newline();
        
        if (depth < this.depth) {
            Collection<ModuleVersionDetails> versions = getModuleVersions(dep.getName(), dep.getVersion(), ModuleQuery.Type.ALL, null);
            if (!versions.isEmpty()) {
                recurseDependencies(versions.iterator().next(), depth + 1);
            }
        }
    }
}
