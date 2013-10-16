package com.redhat.ceylon.tools.info;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.RepositoryManager;
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
            String name = module.getName();
            if (!module.isVersioned() && (name.startsWith("*") || name.endsWith("*"))) {
                Collection<ModuleDetails> modules = getModules(name, ModuleQuery.Type.ALL, null, null);
                if (modules.isEmpty()) {
                    errorMsg("module.not.found", module, getRepositoryManager().getRepositoriesDisplayString());
                    continue;
                }
                outputModules(module, modules);
            } else {
                Collection<ModuleVersionDetails> versions = getModuleVersions(module.getName(), module.getVersion(), ModuleQuery.Type.ALL, null);
                if (versions.isEmpty()) {
                    errorMsg("module.not.found", module, getRepositoryManager().getRepositoriesDisplayString());
                    continue;
                }
                if (module.getVersion() == null || module.getVersion().isEmpty()) {
                    outputVersions(module, versions);
                } else {
                    outputDetails(module, versions.iterator().next());
                }
            }
        }
    }

    private Collection<ModuleDetails> getModules(String name, ModuleQuery.Type type, Integer binaryMajor, Integer binaryMinor) {
        return getModules(getRepositoryManager(), name, type, binaryMajor, binaryMinor);
    }

    private Collection<ModuleDetails> getModules(RepositoryManager repoMgr, String name, ModuleQuery.Type type, Integer binaryMajor, Integer binaryMinor) {
        String queryString = name;
        if (queryString.startsWith("*")) {
            queryString = queryString.substring(1);
        }
        if (queryString.endsWith("*")) {
            queryString = queryString.substring(0, queryString.length() - 1);
        }
        
        ModuleQuery query = new ModuleQuery(queryString, type);
        if (binaryMajor != null) {
            query.setBinaryMajor(binaryMajor);
        }
        if (binaryMinor != null) {
            query.setBinaryMinor(binaryMinor);
        }
        ModuleSearchResult result;
        if (!name.startsWith("*")) {
            result = repoMgr.completeModules(query);
        } else {
            result = repoMgr.searchModules(query);
        }
        return result.getResults();
    }

    private void outputModules(ModuleSpec query, Collection<ModuleDetails> modules) throws IOException {
        msg("module.query").append(query.getName()).newline();
        for (ModuleDetails module : modules) {
            append("    ").append(module.getName()).newline();
            for (ModuleVersionDetails version : module.getVersions()) {
                append("        ").append(version.getVersion());
                if (version.isRemote()) {
                    append(" *");
                }
                newline();
            }
        }
    }

    private void outputVersions(ModuleSpec module, Collection<ModuleVersionDetails> versions) throws IOException {
        msg("version.query").append(module.getName()).newline();
        for (ModuleVersionDetails version : versions) {
            append("    ").append(version.getVersion());
            if (version.isRemote()) {
                append(" *");
            }
            newline();
        }
    }

    private void outputDetails(ModuleSpec module, ModuleVersionDetails version) throws IOException {
        msg("module.name").append(module.getName()).newline();
        msg("module.version").append(version.getVersion()).newline();
        outputArtifacts(version.getArtifactTypes());
        msg("module.available").msg((version.isRemote() ? "available.remote" : "available.local")).newline();
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
                int major = (type.getMajorBinaryVersion() != null) ? type.getMajorBinaryVersion() : 0;
                int minor = (type.getMinorBinaryVersion() != null) ? type.getMinorBinaryVersion() : 0;
                if (suffix.equalsIgnoreCase(".car")) {
                    append("JVM (#");
                    append(major);
                    if (minor != 0) {
                        append(".");
                        append(minor);
                    }
                    if (major != Versions.JVM_BINARY_MAJOR_VERSION && minor != Versions.JVM_BINARY_MINOR_VERSION) {
                        append(" *incompatible*");
                    }
                    append(")");
                } else if (suffix.equalsIgnoreCase(".jar")) {
                    append("JVM (legacy)");
                } else if (suffix.equalsIgnoreCase(".js")) {
                    append("JavaScript (#");
                    append(major);
                    if (minor != 0) {
                        append(".");
                        append(minor);
                    }
                    if (major != Versions.JS_BINARY_MAJOR_VERSION && minor != Versions.JS_BINARY_MINOR_VERSION) {
                        append(" *incompatible*");
                    }
                    append(")");
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
