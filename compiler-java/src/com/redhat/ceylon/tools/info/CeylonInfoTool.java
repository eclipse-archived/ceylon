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
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.ModuleSpec;

@Summary("Prints information about modules in repositories")
@Description("When passed a search query like `*foo*` it will look at all the modules in all " +
        "repositories and see if the word `foo` appears anywhere in the name, description, " +
        "version, license or any other field in the module's desciptor and print their names. " +
        "\n\n" +
        "When passed a partial module name like `com.acme.foo*` it will look at all the " +
        "modules in all the repositoriues and see if their names start with `com.acme.foo` " +
        "and print their names." +
        "\n\n" +
        "When passed a complete module name like `com.acme.foobar` it will print the list " +
        "of available versions for that module. Versions marked with `*` are not currently " +
        "available on the local system but can be downloaded on-demand from remote servers." +
        "\n\n" +
        "When passed a complete module name and version like `com.acme.foobar/1.0` it will " +
        "print information about the contents of a module archive, its description, its licence " +
        "and its dependencies")
public class CeylonInfoTool extends RepoUsingTool {

    private static final int INFINITE_DEPTH = -1; 
    
    private List<ModuleSpec> modules;
    private boolean showVersions;
    private boolean showDependencies;
    private boolean showIncompatible;
    private String showType;
    private int depth = 1;
    private String findMember;
    private String findPackage;
    private boolean showNames;
    private boolean exactMatch;
    
    private Integer binaryMajor = null;
    private Integer binaryMinor = null;
    private ModuleQuery.Type queryType = ModuleQuery.Type.ALL;
    
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
    
    @Option(longName="show-versions")
    @Description("Show the versions when searching for modules")
    public void setShowVersions(boolean showVersions) {
        this.showVersions = showVersions;
    }
    
    @Option(longName="show-dependencies")
    @Description("Show the dependencies whenever versions are shown")
    public void setShowDependencies(boolean showDependencies) {
        this.showDependencies = showDependencies;
    }
    
    @Option(longName="show-incompatible")
    @Description("Also show versions incompatible with the current Ceylon installation")
    public void setShowIncompatible(boolean showIncompatible) {
        this.showIncompatible = showIncompatible;
    }
    
    @OptionArgument(argumentName = "type")
    @Description("The artifact ypes to show information for. " +
            "Allowed values include: `all`, `jvm`, `js`, `src` (default is `all`).")
    public void setShowType(String showType) {
        this.showType = showType;
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
    
    @OptionArgument(argumentName = "member-name")
    @Description("Shows only those modules that contain members whose name match the given argument.")
    public void setFindMember(String findMember) {
        this.findMember = findMember;
    }
    
    @OptionArgument(argumentName = "package-name")
    @Description("Shows only those modules that contain packages whose name match the given argument.")
    public void setFindPackage(String findPackage) {
        this.findPackage = findPackage;
    }
    
    @Option(longName="show-names")
    @Description("Show the matching items when using the `find-member` or `find-package` option")
    public void setShowNames(boolean showNames) {
        this.showNames = showNames;
    }
    
    @Option(longName="exact-match")
    @Description("Only returns exact matches when using the `find-member` or `find-package` option")
    public void setExactMatch(boolean exactMatch) {
        this.exactMatch = exactMatch;
    }

    @Override
    public void initialize() {
        if (showType != null) {
            if ("jvm".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.JVM;
            } else if ("js".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.JS;
            } else if ("src".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.SRC;
            } else if ("all".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.ALL;
            } else {
                throw new IllegalArgumentException(CeylonInfoMessages.msg("illegal.type", showType));
            }
        }
        if (findMember != null && "src".equalsIgnoreCase(showType)) {
            throw new IllegalArgumentException(CeylonInfoMessages.msg("incompatible.query.and.find"));
        }
        if (findMember != null && findPackage != null) {
            throw new IllegalArgumentException(CeylonInfoMessages.msg("incompatible.find.options"));
        }
    }
    
    @Override
    public void run() throws Exception {
        setSystemProperties();
        if (!showIncompatible) {
            binaryMajor = Versions.JVM_BINARY_MAJOR_VERSION;
            binaryMinor = Versions.JVM_BINARY_MINOR_VERSION;
        }
        for (ModuleSpec module : modules) {
            String name = module.getName();
            if (!module.isVersioned() && (name.startsWith("*") || name.endsWith("*"))) {
                Collection<ModuleDetails> modules = getModules(getRepositoryManager(), name, queryType, binaryMajor, binaryMinor);
                if (modules.isEmpty()) {
                    String err = getModuleNotFoundErrorMessage(getRepositoryManager(), module.getName(), module.getVersion());
                    errorAppend(err);
                    errorNewline();
                    continue;
                }
                outputModules(module, modules);
            } else {
                Collection<ModuleVersionDetails> versions = getModuleVersions(getRepositoryManager(), module.getName(), module.getVersion(), queryType, binaryMajor, binaryMinor);
                if (versions.isEmpty()) {
                    String err = getModuleNotFoundErrorMessage(getRepositoryManager(), module.getName(), module.getVersion());
                    errorAppend(err);
                    errorNewline();
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

    private Collection<ModuleDetails> getModules(RepositoryManager repoMgr, String name, ModuleQuery.Type type, Integer binaryMajor, Integer binaryMinor) {
        String queryString = name;
        if (queryString.startsWith("*")) {
            queryString = queryString.substring(1);
        }
        if (queryString.endsWith("*")) {
            queryString = queryString.substring(0, queryString.length() - 1);
        }
        
        ModuleVersionQuery query = getModuleVersionQuery(queryString, null, type, binaryMajor, binaryMinor);
        
        ModuleSearchResult result;
        if (!name.startsWith("*") || name.equals("*")) {
            result = repoMgr.completeModules(query);
        } else {
            result = repoMgr.searchModules(query);
        }
        return result.getResults();
    }

    @Override
    protected ModuleVersionQuery getModuleVersionQuery(String name, String version, ModuleQuery.Type type, Integer binaryMajor, Integer binaryMinor) {
        ModuleVersionQuery query = super.getModuleVersionQuery(name, version, type, binaryMajor, binaryMinor);
        if (findMember != null) {
            query.setMemberName(findMember);
        }
        if (findPackage != null) {
            query.setMemberName(findPackage);
            query.setMemberSearchPackageOnly(true);
        }
        query.setMemberSearchExact(exactMatch);
        return query;
    }
    
    private void outputModules(ModuleSpec query, Collection<ModuleDetails> modules) throws IOException {
        if (findMember == null) {
            msg("module.query", query.getName()).newline();
        } else {
            msg("module.query.find", query.getName(), findMember).newline();
        }
        for (ModuleDetails module : modules) {
            append("    ").append(module.getName()).newline();
            if (showVersions) {
                outputVersions(module.getVersions(), "        ");
            } else if (showNames) {
                outputNames(module.getLastVersion(), "        ");
            }
        }
    }

    private void outputVersions(ModuleSpec module, Collection<ModuleVersionDetails> versions) throws IOException {
        if (findMember == null) {
            msg("version.query", module.getName()).newline();
        } else {
            msg("version.query.find", module.getName(), findMember).newline();
        }
        outputVersions(versions, "    ");
    }

    private void outputVersions(Collection<ModuleVersionDetails> versions, String prefix) throws IOException {
        for (ModuleVersionDetails version : versions) {
            append(prefix).append(version.getVersion());
            if (version.isRemote()) {
                append(" *");
            }
            newline();
            if (showDependencies) {
                for (ModuleInfo dep : version.getDependencies()) {
                    append(prefix).append("    ").append(dep).newline();
                }
            }
            if (showNames) {
                outputNames(version, prefix + "    ");
            }
        }
    }

    private void outputNames(ModuleVersionDetails version, String prefix) throws IOException {
        for (String member : version.getMembers()) {
            append(prefix).append(member).newline();
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
            Collection<ModuleVersionDetails> versions = getModuleVersions(dep.getName(), dep.getVersion(), queryType, binaryMajor, binaryMinor);
            if (!versions.isEmpty()) {
                recurseDependencies(versions.iterator().next(), depth + 1);
            }
        }
    }
}
