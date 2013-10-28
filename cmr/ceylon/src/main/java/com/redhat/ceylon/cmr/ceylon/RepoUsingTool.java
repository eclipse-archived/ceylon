package com.redhat.ceylon.cmr.ceylon;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NavigableMap;
import java.util.ResourceBundle;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.common.Messages;
import com.redhat.ceylon.common.ModuleDescriptorReader;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.CeylonBaseTool;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ServiceToolLoader;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;

public abstract class RepoUsingTool extends CeylonBaseTool {
    protected List<URI> repo;
    protected String systemRepo;
    protected String cacheRepo;
    protected String mavenOverrides;
    protected boolean noDefRepos;
    protected boolean offline;

    private RepositoryManager rm;
    
    private Appendable out = System.out;
    private Appendable error = System.err;
    
    private ResourceBundle bundle;
    
    private static final List<String> EMPTY_STRINGS = new ArrayList<String>(0);
    private static final List<URI> EMPTY_URIS = new ArrayList<URI>(0);
    
    public RepoUsingTool(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    public List<String> getRepositoryAsStrings() {
        if (repo != null) {
            List<String> result = new ArrayList<String>(repo.size());
            for (URI uri : repo) {
                result.add(uri.toString());
            }
            return result;
        } else {
            return EMPTY_STRINGS;
        }
    }
    
    public void setRepositoryAsStrings(List<String> repo) throws Exception {
        if (repo != null) {
            List<URI> result = new ArrayList<URI>(repo.size());
            for (String r : repo) {
                result.add(StandardArgumentParsers.URI_PARSER.parse(r, this));
            }
            setRepository(result);
        } else {
            setRepository(EMPTY_URIS);
        }
    }

    @OptionArgument(longName="rep", argumentName="url")
    @Description("Specifies a module repository containing dependencies. Can be specified multiple times. " +
            "(default: `modules`, `~/.ceylon/repo`, http://modules.ceylon-lang.org)")
    public void setRepository(List<URI> repo) {
        this.repo = repo;
    }
    
    @OptionArgument(longName="sysrep", argumentName="url")
    @Description("Specifies the system repository containing essential modules. " +
            "(default: `$CEYLON_HOME/repo`)")
    public void setSystemRepository(String systemRepo) {
        this.systemRepo = systemRepo;
    }
    
    @OptionArgument(longName="cacherep", argumentName="url")
    @Description("Specifies the folder to use for caching downloaded modules. " +
            "(default: `~/.ceylon/cache`)")
    public void setCacheRepository(String cacheRepo) {
        this.cacheRepo = cacheRepo;
    }

    @OptionArgument(longName="maven-overrides", argumentName="url")
    @Description("Specifies the xml file to use to load Maven artifact overrides.")
    public void setMavenOverrides(String mavenOverrides) {
        this.mavenOverrides = mavenOverrides;
    }

    @Option(longName="no-default-repositories")
    @Description("Indicates that the default repositories should not be used.")
    public void setNoDefRepos(boolean noDefRepos) {
        this.noDefRepos = noDefRepos;
    }

    @Option(longName="offline")
    @Description("Enables offline mode that will prevent connections to remote repositories.")
    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public void setOut(Appendable out) {
        this.out = out;
    }
    
    protected CeylonUtils.CeylonRepoManagerBuilder createRepositoryManagerBuilder() {
        CeylonUtils.CeylonRepoManagerBuilder rmb = CeylonUtils.repoManager()
                .cwd(cwd)
                .mavenOverrides(mavenOverrides)
                .systemRepo(systemRepo)
                .cacheRepo(cacheRepo)
                .noDefaultRepos(noDefRepos)
                .userRepos(getRepositoryAsStrings())
                .offline(offline);
        return rmb;
    }
    
    protected synchronized RepositoryManager getRepositoryManager() {
        if (rm == null) {
            CeylonUtils.CeylonRepoManagerBuilder rmb = createRepositoryManagerBuilder();
            rm = rmb.buildManager();   
        }
        return rm;
    }
    
    protected Collection<ModuleVersionDetails> getModuleVersions(String name, String version, ModuleQuery.Type type, Integer binaryVersion) {
        return getModuleVersions(getRepositoryManager(), name, version, type, binaryVersion);
    }

    protected Collection<ModuleVersionDetails> getModuleVersions(RepositoryManager repoMgr, String name, String version, ModuleQuery.Type type, Integer binaryVersion) {
        ModuleVersionQuery query = new ModuleVersionQuery(name, version, type);
        if (binaryVersion != null) {
            query.setBinaryMajor(binaryVersion);
        }
        ModuleVersionResult result = repoMgr.completeVersions(query);
        NavigableMap<String, ModuleVersionDetails> versionMap = result.getVersions();
        return versionMap.values();
    }

    protected String checkModuleVersionsOrShowSuggestions(RepositoryManager repoMgr, String name, String version, ModuleQuery.Type type, Integer binaryVersion) throws IOException {
        return checkModuleVersionsOrShowSuggestions(repoMgr, name, version, type, binaryVersion, COMPILE_NEVER);
    }
    
    protected static final String COMPILE_NEVER = "never";
    protected static final String COMPILE_ONCE = "once";
    protected static final String COMPILE_FORCE = "force";
    
    protected String checkModuleVersionsOrShowSuggestions(RepositoryManager repoMgr, String name, String version, ModuleQuery.Type type, Integer binaryVersion, String compileFlags) throws IOException {
        if (compileFlags == null) {
            compileFlags = DefaultToolOptions.getRunnerCompileFlags();
            if (compileFlags.isEmpty()) {
                compileFlags = COMPILE_NEVER;
            }
        } else if (compileFlags.isEmpty()) {
            compileFlags = COMPILE_ONCE;
        }
        boolean forceCompilation = compileFlags.contains("force");
        boolean checkCompilation = compileFlags.contains("check");
        boolean allowCompilation = forceCompilation || checkCompilation || compileFlags.contains("once");
        
        if (!forceCompilation && ("default".equals(name) || version != null)) {
            // If we have the default module or a version we first try it the quick way
            ArtifactContext ac = new ArtifactContext(name, version, type.getSuffixes());
            ac.setFetchSingleArtifact(true);
            ac.setThrowErrorIfMissing(false);
            ArtifactResult result = repoMgr.getArtifactResult(ac);
            if (result != null) {
                return (result.version() != null) ? result.version() : "";
            }
            if ("default".equals(name)) {
                errorMsg("module.not.found", name, repoMgr.getRepositoriesDisplayString());
                return null;
            }
        }
        
        boolean suggested = false;
        Collection<ModuleVersionDetails> versions = getModuleVersions(repoMgr, name, version, type, binaryVersion);
        if (version != null) {
            // Here we either have a single version or none
            if (versions.isEmpty() || forceCompilation || shouldRecompile(checkCompilation, repoMgr, name, version, type)) {
                if (allowCompilation) {
                    Collection<ModuleVersionDetails> srcVersions = getVersionFromSource(name);
                    if (!srcVersions.isEmpty() && version.equals(srcVersions.iterator().next().getVersion())) {
                        // There seems to be source code that has the proper version
                        // Let's see if we can compile it...
                        if (runCompiler(repoMgr, name, type)) {
                            // All okay it seems, let's use this version
                            versions = srcVersions;
                        }
                    }
                }
                if (versions.isEmpty()) {
                    // Maybe the user specified the wrong version?
                    // Let's see if we can find any and suggest them
                    versions = getModuleVersions(repoMgr, name, null, type, binaryVersion);
                    suggested = true;
                }
            }
        } else {
            // Here we can have any number of versions, including none
            if (allowCompilation
                    && (versions.isEmpty()
                        || onlyRemote(versions)
                        || forceCompilation
                        || checkCompilation)) {
                // If there are no versions at all or only in remote repositories we
                // first check if there's local code we could compile before giving up
                Collection<ModuleVersionDetails> srcVersions = getVersionFromSource(name);
                if (!srcVersions.isEmpty()) {
                    // There seems to be source code
                    // Let's see if we can compile it...
                    String srcver = srcVersions.iterator().next().getVersion();
                    if (!checkCompilation || shouldRecompile(checkCompilation, repoMgr, name, srcver, type)) {
                        if (runCompiler(repoMgr, name, type)) {
                            // All okay it seems, let's use this version
                            versions = srcVersions;
                        }
                    }
                }
            }
        }
        if (versions.isEmpty()) {
            errorMsg("module.not.found", name, repoMgr.getRepositoriesDisplayString());
            return null;
        }
        if (versions.size() > 1 || suggested) {
            if (version == null) {
                errorMsg("missing.version", name, repoMgr.getRepositoriesDisplayString());
            } else {
                errorMsg("version.not.found", version, name);
            }
            msg(error, "try.versions");
            boolean first = true;
            for (ModuleVersionDetails mvd : versions) {
                if (!first) {
                    append(error, ", ");
                }
                append(error, mvd.getVersion());
                if (mvd.isRemote()) {
                    append(error, " (*)");
                }
                first = false;
            }
            newline(error);
            return null;
        }
        return versions.iterator().next().getVersion();
    }
    
    private boolean shouldRecompile(boolean checkCompilation, RepositoryManager repoMgr, String name, String version, ModuleQuery.Type type) throws IOException {
        if (checkCompilation) {
            ArtifactContext ac = new ArtifactContext(name, version, type.getSuffixes());
            ac.setFetchSingleArtifact(true);
            ac.setThrowErrorIfMissing(false);
            File artifile = repoMgr.getArtifact(ac);
            long oldestInCar = JarUtils.oldestFileTime(artifile);
            long newestSource = getNewestLastmodified(name);
            if (newestSource > oldestInCar) {
                return true;
            }
        }
        return false;
    }

    private long getNewestLastmodified(String name) throws IOException {
        final long[] newest = new long[] { -1L };
        List<File> srcDirs = DefaultToolOptions.getCompilerSourceDirs();
        for (File srcDir : srcDirs) {
            File moduleDir = new File(srcDir, name);
            Files.walkFileTree(moduleDir.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (newest[0] == -1L || file.toFile().lastModified() > newest[0]) {
                        newest[0] = file.toFile().lastModified();
                    }
                    return FileVisitResult.CONTINUE;
                }
                
            });
        }
        return newest[0];
    }
    
    private boolean onlyRemote(Collection<ModuleVersionDetails> versions) {
        for (ModuleVersionDetails version : versions) {
            if (!version.isRemote()) {
                return false;
            }
        }
        return true;
    }
    
    private Collection<ModuleVersionDetails> getVersionFromSource(String name) {
        Collection<ModuleVersionDetails> result = new ArrayList<ModuleVersionDetails>();
        try {
            List<File> srcDirs = DefaultToolOptions.getCompilerSourceDirs();
            for (File srcDir : srcDirs) {
                ModuleDescriptorReader mdr = new ModuleDescriptorReader(name, srcDir);
                String version = mdr.getModuleVersion();
                if (version != null) {
                    ModuleVersionDetails mvd = new ModuleVersionDetails(version);
                    mvd.setLicense(mdr.getModuleLicense());
                    List<String> by = mdr.getModuleAuthors();
                    if (by != null) {
                        mvd.getAuthors().addAll(by);
                    }
                    mvd.setRemote(false);
                    mvd.setOrigin("Local source folder");
                    result.add(mvd);
                    break;
                }
            }
        } catch (Exception ex) {
            // Just continue as if nothing happened
        }
        return result;
    }
    
    private boolean runCompiler(RepositoryManager repoMgr, String name, ModuleQuery.Type type) {
        List<String> args = new ArrayList<String>();
        if (systemRepo != null) {
            args.add("--sysrep");
            args.add(systemRepo);
        }
        if (cacheRepo != null) {
            args.add("--cacherep");
            args.add(cacheRepo);
        }
        if (repo != null) {
            for (URI r : repo) {
                args.add("--rep");
                args.add(r.toString());
            }
        }
        if (offline) {
            args.add("--offline");
        }
        args.add(name);
        
        ToolFactory pluginFactory = new ToolFactory();
        ToolLoader pluginLoader = new ServiceToolLoader(Tool.class) {
            @Override
            public String getToolName(String className) {
                return camelCaseToDashes(className.replaceAll("^(.*\\.)?Ceylon(.*)Tool$", "$2"));
            }
        };
        String toolName;
        if (type == ModuleQuery.Type.JVM) {
            toolName = "compile";
        } else if (type == ModuleQuery.Type.JS) {
            toolName = "compile-js";
        } else {
            throw new IllegalArgumentException("Unknown compile flags passed");
        }
        ToolModel<Tool> model = pluginLoader.loadToolModel(toolName);
        Tool tool = pluginFactory.bindArguments(model, args);
        try {
            msg("compiling").newline();
            tool.run();
            // Make sure we can find the newly created module
            repoMgr.refresh(false);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public RepoUsingTool errorMsg(String msgKey, Object...msgArgs) throws IOException {
        error.append(Messages.msg(bundle, msgKey, msgArgs));
        error.append(System.lineSeparator());
        return this;
    }
    
    public RepoUsingTool msg(Appendable out, String msgKey, Object...msgArgs) throws IOException {
        out.append(Messages.msg(bundle, msgKey, msgArgs));
        return this;
    }
    
    public RepoUsingTool msg(String msgKey, Object...msgArgs) throws IOException {
        return msg(out, msgKey, msgArgs);
    }
    
    public RepoUsingTool append(Appendable out, Object s) throws IOException {
        out.append(String.valueOf(s));
        return this;
    }
    
    public RepoUsingTool append(Object s) throws IOException {
        return append(out, s);
    }
    
    public RepoUsingTool newline(Appendable out) throws IOException {
        out.append(System.lineSeparator());
        return this;
    }
    
    public RepoUsingTool newline() throws IOException {
        return newline(out);
    }
}
