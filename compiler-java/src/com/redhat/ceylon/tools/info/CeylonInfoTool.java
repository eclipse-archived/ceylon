package com.redhat.ceylon.tools.info;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.VersionComparator;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ParsedBy;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
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
    
    public enum Formatting {
        simple, fancy
    }
    
    private List<ModuleSpec> modules;
    private boolean showVersions;
    private boolean showDependencies;
    private boolean showIncompatible;
    private boolean showFullDescription;
    private String showType;
    private int depth = 1;
    private String findMember;
    private String findPackage;
    private boolean showNames;
    private boolean exactMatch;
    private boolean requireAll;
    private boolean printOverrides;
    private Formatting formatting;
    private List<File> sourceFolders = DefaultToolOptions.getCompilerSourceDirs();

    private Integer binaryMajor = null;
    private Integer binaryMinor = null;
    private ModuleQuery.Type queryType = ModuleQuery.Type.ALL;
    
    public CeylonInfoTool() {
        super(CeylonInfoMessages.RESOURCE_BUNDLE);
    }
    
    @Override
    protected boolean includeJDK() {
        return true;
    }
    
    @Override
    protected List<File> getSourceDirs() {
        return sourceFolders;
    }
    
    @OptionArgument(longName="src", argumentName="dir")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("A directory containing Ceylon and/or Java source code (default: `./source`)")
    public void setSourceFolders(List<File> sourceFolders) {
        this.sourceFolders = sourceFolders;
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

    @Option(longName="print-overrides")
    @Description("Print a usable module overrides file when there are duplicate versions, selecting the latest versions")
    public void setPrintOverrides(boolean printOverrides) {
        this.printOverrides = printOverrides;
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
    
    @Option(longName="show-full-description")
    @Description("Shows the full description for module details")
    public void setShowFullDescription(boolean showFullDescription) {
        this.showFullDescription = showFullDescription;
    }
    
    @Option(longName="require-all")
    @Description("Only show those results that have all the requested artifact types")
    public void setRequireAll(boolean requireAll) {
        this.requireAll = requireAll;
    }
    
    @OptionArgument(argumentName = "type")
    @Description("The artifact types to show information for. " +
            "Allowed values include: `all`, `jvm`, `car`, `jar`, `js`, `src`, `code`, `ceylon` (default is `all`).")
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

    @OptionArgument(argumentName = "formatting")
    @Description("Set the output formatting to use, can be `simple` or `fancy`")
    public void setFormatting(Formatting formatting) {
        this.formatting = formatting;
    }

    @Override
    protected boolean needsSystemRepo() {
        return false;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
        if (showType != null) {
            if ("car".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.CAR;
            } else if ("jar".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.JAR;
            } else if ("jvm".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.JVM;
            } else if ("js".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.JS;
            } else if ("src".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.SRC;
            } else if ("all".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.ALL;
            } else if ("code".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.CODE;
            } else if ("ceylon".equalsIgnoreCase(showType)) {
                queryType = ModuleQuery.Type.CEYLON_CODE;
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
        if (formatting == null) {
            formatting = (System.console() != null) ? Formatting.fancy : Formatting.simple;
        }
    }
    
    @Override
    public void run() throws Exception {
        if (!showIncompatible) {
            binaryMajor = Versions.JVM_BINARY_MAJOR_VERSION;
            binaryMinor = Versions.JVM_BINARY_MINOR_VERSION;
        }
        
        for (ModuleSpec module : modules) {
            String name = module.getName();
            if (!module.isVersioned() && (name.startsWith("*") || name.endsWith("*"))) {
                Collection<ModuleDetails> modules = getModules(getRepositoryManager(), name, queryType, binaryMajor, binaryMinor);
                if (modules.isEmpty()) {
                    String err;
                    if (name.startsWith("*") || name.endsWith("*")) {
                        err = CeylonInfoMessages.msg("no.match", name);
                    } else {
                        err = getModuleNotFoundErrorMessage(getRepositoryManager(), module.getName(), module.getVersion());
                    }
                    errorAppend(err);
                    errorNewline();
                    continue;
                }
                outputModules(module, modules);
            } else {
                Collection<ModuleVersionDetails> versions = getModuleVersions(getRepositoryManager(), module.getName(), module.getVersion(), queryType, binaryMajor, binaryMinor);
                if (versions.isEmpty()) {
                    // try from source
                    ModuleVersionDetails fromSource = getVersionFromSource(name);
                    if(fromSource != null){
                        // is it the version we're after?
                        versions = Arrays.asList(fromSource);
                    }else{
                        String err = getModuleNotFoundErrorMessage(getRepositoryManager(), module.getName(), module.getVersion());
                        errorAppend(err);
                        errorNewline();
                        continue;
                    }
                }
                if (module.getVersion() == null || module.getVersion().isEmpty() || versions.size() > 1) {
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
        if (requireAll) {
            query.setRetrieval(ModuleQuery.Retrieval.ALL);
        }
        return query;
    }
    
    private void outputModules(ModuleSpec query, Collection<ModuleDetails> modules) throws IOException {
        if (formatting == Formatting.fancy) {
            if (findMember != null) {
                msg("module.query.member", query.getName(), findMember).newline();
            } else if (findPackage != null) {
                msg("module.query.package", query.getName(), findPackage).newline();
            } else {
                msg("module.query", query.getName()).newline();
            }
        }
        outputModules(modules);
    }

    private void outputModules(Collection<ModuleDetails> modules) throws IOException {
        for (ModuleDetails module : modules) {
            outputModule(module);
        }
    }

    private void outputModule(ModuleDetails module) throws IOException {
        String prefix = (formatting == Formatting.fancy) ? "    " : "";
        if (formatting == Formatting.fancy || (!showVersions && !showNames)) {
            append(prefix).append(module.getName()).newline();
        }
        if (showVersions) {
            outputVersions(module.getName(), module.getVersions(), prefix + prefix);
        } else if (showNames) {
            outputNames(module.getName(), module.getLastVersion(), prefix + prefix);
        }
    }

    private void outputVersions(ModuleSpec module, Collection<ModuleVersionDetails> versions) throws IOException {
        if (formatting == Formatting.fancy) {
            if (findMember != null) {
                msg("version.query.member", module.getName(), findMember).newline();
            } else if (findPackage != null) {
                msg("version.query.package", module.getName(), findPackage).newline();
            } else {
                msg("version.query", module.getName()).newline();
            }
        }
        outputVersions(module.getName(), versions, "    ");
    }

    private void outputVersions(String moduleName, Collection<ModuleVersionDetails> versions, String prefix) throws IOException {
        String namePrefix = (formatting == Formatting.fancy) ? prefix + "    " : "";
        for (ModuleVersionDetails version : versions) {
            if (formatting == Formatting.fancy || (!showDependencies && !showNames)) {
                append(prefix);
                if (formatting == Formatting.simple) {
                    append(moduleName).append("/");
                }
                append(version.getVersion());
                if (version.isRemote() && formatting == Formatting.fancy) {
                    append(" *");
                }
                newline();
            }
            if (showDependencies) {
                if (formatting == Formatting.fancy || !version.getDependencies().isEmpty()) {
                    for (ModuleDependencyInfo dep : version.getDependencies()) {
                        if (formatting == Formatting.fancy) {
                            append(prefix).append("    ").append(dep);
                        } else {
                            append(moduleName).append("/").append(version.getVersion()).append(" ").append(dep.getModuleName());
                        }
                        newline();
                    }
                } else {
                    append(moduleName).append("/").append(version.getVersion()).newline();
                }
            }
            if (showNames) {
                outputNames(moduleName, version, namePrefix);
            }
        }
    }

    private void outputNames(String moduleName, ModuleVersionDetails version, String prefix) throws IOException {
        for (String member : version.getMembers()) {
            if (formatting == Formatting.fancy) {
                append(prefix).append(member).newline();
            } else {
                append(moduleName);
                if (showVersions) {
                    append("/").append(version.getVersion());
                }
                append("::").append(member).newline();
            }
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
            String docs = version.getDoc();
            if (!showFullDescription) {
                docs = summary(version.getDoc());
            }
            msg("module.description").append(docs).newline();
        }
        if (version.getLicense() != null) {
            msg("module.license").append(version.getLicense()).newline();
        }
        if (version.getAuthors() != null && !version.getAuthors().isEmpty()) {
            outputAuthors(version.getAuthors());
        }
        if (!version.getDependencies().isEmpty()) {
            msg("module.dependencies.tree", (depth == INFINITE_DEPTH ? "∞" : String.valueOf(depth))).newline();
            SortedMap<String, SortedSet<String>> names = new TreeMap<>();
            recurseDependencies(version, names, 0);

            if (depth != 1) {
                newline();
                msg("module.dependencies.flat", (depth == INFINITE_DEPTH ? "∞" : String.valueOf(depth))).newline();
                listDependencies(names);
            }
            
            listDependencyConflictsIfAny(names);
        }
    }

    private void listDependencyConflictsIfAny(SortedMap<String, SortedSet<String>> names) throws IOException {
        boolean hasNoDuplicate = true;
        StringBuilder overridesFile = null;
        if(printOverrides){
            overridesFile = new StringBuilder();
            overridesFile.append("<overrides>\n");
        }
        for(Map.Entry<String,SortedSet<String>> entry : names.entrySet()){
            if(entry.getValue().size() > 1){
                if(hasNoDuplicate){
                    hasNoDuplicate = false;
                    newline();
                    msg("module.dependencies.conflicts", (depth == INFINITE_DEPTH ? "∞" : String.valueOf(depth))).newline();
                }
                append("  ");
                append(entry.getKey());
                if(entry.getValue().size() > 1){
                    append(": ");
                    boolean first = true;
                    for(String v : entry.getValue()){
                        if(first)
                            first = false;
                        else
                            append(", ");
                        append(v);
                    }
                    if(printOverrides){
                        overridesFile.append(" <set");
                        String name = entry.getKey();
                        if(name.contains(":")){
                            int p = name.indexOf(':');
                            String groupId = name.substring(0, p);
                            String artifactId = name.substring(p+1);
                            overridesFile.append(" groupId=\"").append(groupId).append("\" artifactId=\"").append(artifactId).append("\"");
                        }else{
                            overridesFile.append(" module=\"").append(name).append("\"");
                        }
                        overridesFile.append(" version=\"").append(entry.getValue().last()).append("\"/>\n");
                    }
                }
                newline();
            }
        }
        if(printOverrides && !hasNoDuplicate){
            overridesFile.append("</overrides>\n");
            newline();
            msg("module.dependencies.overrides").newline();
            newline();
            append(overridesFile.toString());
        }
    }

    private void listDependencies(SortedMap<String, SortedSet<String>> names) throws IOException {
        for(Map.Entry<String,SortedSet<String>> entry : names.entrySet()){
            append("  ");
            append(entry.getKey());
            if(entry.getValue().size() > 1){
                append(": ");
                boolean first = true;
                for(String v : entry.getValue()){
                    if(first)
                        first = false;
                    else
                        append(", ");
                    append(v);
                }
            }else{
                append("/");
                append(entry.getValue().first());
            }
            newline();
        }
    }

    private String summary(String doc) {
        StringBuilder result = new StringBuilder();
        String[] lines = doc.split("\n");
        for (int i = 0; i < lines.length && i < 5; i++) {
            result.append(lines[i]).append('\n');
        }
        if (lines.length > 5) {
            result.append("...").append('\n');
        }
        return result.toString();
    }

    private void outputAuthors(NavigableSet<String> authors) throws IOException {
        msg("module.authors");
        boolean first = true;
        for (String author : authors) {
            if (!first) {
                append(", ");
            }
            append(author);
            first = false;
        }
        newline();
    }

    private RepoUsingTool outputArtifacts(Set<ModuleVersionArtifact> types) throws IOException {
        if(!types.isEmpty()) {
            msg("module.artifacts");
            boolean skipComma = true;
            boolean js = false;
            boolean docs = false;
            for (ModuleVersionArtifact type : types) {
                if (!skipComma) {
                    append(", ");
                }
                String suffix = type.getSuffix();
                int major = (type.getMajorBinaryVersion() != null) ? type.getMajorBinaryVersion() : 0;
                int minor = (type.getMinorBinaryVersion() != null) ? type.getMinorBinaryVersion() : 0;
                if (suffix.equalsIgnoreCase(ArtifactContext.CAR)) {
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
                } else if (suffix.equalsIgnoreCase(ArtifactContext.JAR)) {
                    append("JVM (legacy)");
                } else if (suffix.equalsIgnoreCase(ArtifactContext.JS) || suffix.equalsIgnoreCase(ArtifactContext.JS_MODEL)) {
                    if (js) {
                        skipComma = true;
                        continue;
                    }
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
                    js = true;
                } else if (suffix.equalsIgnoreCase(ArtifactContext.RESOURCES)) {
                    append("JS Resources");
                } else if (suffix.equalsIgnoreCase(ArtifactContext.DOCS)) {
                    if (docs) {
                        skipComma = true;
                        continue;
                    }
                    append("Documentation");
                    docs = true;
                } else if (suffix.equalsIgnoreCase(ArtifactContext.SCRIPTS_ZIPPED)) {
                    append("Script Plugins");
                } else if (suffix.equalsIgnoreCase(ArtifactContext.SRC)) {
                    append("Sources");
                } else if (suffix.startsWith(".")) {
                    append(suffix.substring(1).toUpperCase());
                } else {
                    // We might need to add some special cases
                    // for these in the above list
                    append(type);
                }
                skipComma = false;
            }
            newline();
        }
        return this;
    }
    
    private void recurseDependencies(ModuleVersionDetails version, Map<String, SortedSet<String>> names, final int depth) throws IOException {
        for (ModuleDependencyInfo dep : version.getDependencies()) {
            dependency(dep, names, depth+1);
        }
    }
    
    private void dependency(ModuleDependencyInfo dep, Map<String, SortedSet<String>> names, final int depth) throws IOException {
        for (int ii = 0; ii < depth; ii++) {
            append("  ");
        }
        append(dep);
        SortedSet<String> seenVersions = names.get(dep.getName());
        boolean recurse = this.depth == -1 || depth < this.depth;
        if(seenVersions != null){
            // already seen
            if(seenVersions.size() == 1 && seenVersions.first().equals(dep.getVersion()))
                append(" (already imported)");
            else
                append(" (already imported other version)");
            recurse = false;
        }else{
            seenVersions = new TreeSet<>(VersionComparator.INSTANCE);
            names.put(dep.getName(), seenVersions);
        }
        seenVersions.add(dep.getVersion());
        newline();
        
        if (recurse && !"ceylon.language".equals(dep.getName())) {
            Collection<ModuleVersionDetails> versions = getModuleVersions(dep.getName(), dep.getVersion(), queryType, binaryMajor, binaryMinor);
            if (!versions.isEmpty()) {
                recurseDependencies(versions.iterator().next(), names, depth + 1);
            }
        }
    }
}
