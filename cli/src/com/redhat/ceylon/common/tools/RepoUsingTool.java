package com.redhat.ceylon.common.tools;

import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.ResourceBundle;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.CMRJULLogger;
import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.ContentStore;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.Messages;
import com.redhat.ceylon.common.ModuleDescriptorReader;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.common.tool.ArgumentParser;
import com.redhat.ceylon.common.tool.CeylonBaseTool;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.NonFatalToolMessage;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ParsedBy;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.model.cmr.ArtifactResult;

public abstract class RepoUsingTool extends CeylonBaseTool {
    protected List<URI> repos;
    protected String systemRepo;
    protected String cacheRepo;
    protected String overrides;
    protected int timeout = -1;
    protected boolean noDefRepos;
    protected boolean offline;
    protected List<String> compilerArguments = DefaultToolOptions.getCompilerArguments();
    
    private RepositoryManager rm;
    private RepositoryManager rmoffline;
    private ModuleVersionReader mvr;
    private Logger log;
    
    private Appendable out = System.out;
    private Appendable error = System.err;
    
    protected ResourceBundle bundle;
    
    private static final List<String> EMPTY_STRINGS = new ArrayList<String>(0);
    private static final List<URI> EMPTY_URIS = new ArrayList<URI>(0);
    
    private static final String DOCSECTION_FLAGS = " - **never** - Never perform any compilation\n" +
            " - **once** - Only compile when the compiled module is not available\n" +
            " - **check** - Compile when the sources are newer than the compiled module\n" +
            " - **force** - Always compile" +
            "\n\n" +
            "If the flag is given without an argument it's the same as specifying `check`. " +
            "If no flag is given at all it's the same as specifying `never`.\n";

    public static final String DOCSECTION_COMPILE_FLAGS = "## Compile flags" +
            "\n\n" +
            "The `--compile` option can take the following flags: " +
            "\n\n" +
            DOCSECTION_FLAGS;

    public static final String DOCSECTION_INCLUDE_DEPS = "## Compiling dependencies" +
            "\n\n" +
            "The `--include-dependencies` option can take the following flags: " +
            "\n\n" +
            DOCSECTION_FLAGS;

    public static class TimeoutParser implements ArgumentParser<Integer> {
        @Override
        public Integer parse(String argument, Tool tool) {
            int fact = 1000;
            if (argument.endsWith("ms")) {
                argument = argument.substring(0, argument.length() - 2);
                fact = 1;
            }
            return Integer.valueOf(argument) * fact;
        }
    }

    public RepoUsingTool(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    protected Logger createLogger() {
        return new CMRJULLogger();
    }

    public List<String> getRepositoryAsStrings() {
        if (repos != null) {
            List<String> result = new ArrayList<String>(repos.size());
            for (URI uri : repos) {
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
            "(default: `modules`, `~/.ceylon/repo`, `"+Constants.REPO_URL_CEYLON+"`)")
    public void setRepository(List<URI> repos) {
        this.repos = repos;
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

    // Backwards-compat
    @OptionArgument(longName="maven-overrides", argumentName="file")
    @Description("Specifies the XML file to use to load Maven artifact overrides. See http://ceylon-lang.org/documentation/current/reference/repository/maven/ for information. Deprecated: use --overrides.")
    @Deprecated
    public void setMavenOverrides(String mavenOverrides) {
        this.overrides = mavenOverrides;
    }

    @OptionArgument(shortName='O', longName="overrides", argumentName="file")
    @Description("Specifies the XML file to use to load module overrides. See http://ceylon-lang.org/documentation/current/reference/repository/maven/ for information. *Experimental*.")
    public void setOverrides(String overrides) {
        this.overrides = overrides;
    }

    @Option(longName="no-default-repositories")
    @Description("Indicates that the default repositories should not be used.")
    public void setNoDefRepos(boolean noDefRepos) {
        this.noDefRepos = noDefRepos;
    }

    @Option(shortName='L', longName="offline")
    @Description("Enables offline mode that will prevent connections to remote repositories.")
    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    @ParsedBy(TimeoutParser.class)
    @OptionArgument(shortName='T', longName="timeout", argumentName="seconds")
    @Description("Sets the timeout for connections to remote repositories, use 0 for no timeout (default: 20).")
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setOut(Appendable out) {
        this.out = out;
    }
    
    // Tools that don't necessarily need the system repository
    // to function can override this and set it to "false".
    // If the user then specifies "--no-default-repositories"
    // on the command line the system repository will be removed
    // as well
    protected boolean needsSystemRepo() {
        return true;
    }
    
    /**
     * For subclasses that do not want to read from the output repo
     */
    protected boolean doNotReadFromOutputRepo() {
        return true;
    }
    
    /**
     * For subclasses that want to override the behavior of distribution linking
     */
    protected boolean shouldUpgradeDist() {
        return true;
    }
    
    public Logger getLogger() {
        if (log == null) {
            log = createLogger();
        }
        return log;
    }
    
    protected CeylonUtils.CeylonRepoManagerBuilder createRepositoryManagerBuilder() {
        CeylonUtils.CeylonRepoManagerBuilder rmb = CeylonUtils.repoManager()
                .cwd(cwd)
                .overrides(overrides)
                .noSystemRepo(!needsSystemRepo() && noDefRepos)
                .noCacheRepo(noDefRepos && offline)
                .noOutRepo(noDefRepos || doNotReadFromOutputRepo())
                .systemRepo(systemRepo)
                .cacheRepo(cacheRepo)
                .noDefaultRepos(noDefRepos)
                .userRepos(getRepositoryAsStrings())
                .offline(offline)
                .timeout(timeout)
                .isJDKIncluded(includeJDK())
                .upgradeDist(shouldUpgradeDist())
                .logger(getLogger());
        return rmb;
    }
    
    protected boolean includeJDK() {
        return false;
    }

    protected synchronized RepositoryManager getRepositoryManager() {
        if (rm == null) {
            CeylonUtils.CeylonRepoManagerBuilder rmb = createRepositoryManagerBuilder();
            rm = rmb.buildManager();   
        }
        return rm;
    }
    
    protected synchronized RepositoryManager getOfflineRepositoryManager() {
        if (offline) {
            return getRepositoryManager();
        }
        if (rmoffline == null) {
            CeylonUtils.CeylonRepoManagerBuilder rmb = createRepositoryManagerBuilder();
            rmb.offline(true);
            rmoffline = rmb.buildManager();   
        }
        return rmoffline;
    }
    
    protected ModuleVersionQuery getModuleVersionQuery(String namespace, String name, String version, ModuleQuery.Type type, 
    		Integer jvmBinaryMajor, Integer jvmBinaryMinor,
    		Integer jsBinaryMajor, Integer jsBinaryMinor) {
        ModuleVersionQuery query = new ModuleVersionQuery(namespace, name, version, type);
        if (jvmBinaryMajor != null) {
            query.setJvmBinaryMajor(jvmBinaryMajor);
        }
        if (jvmBinaryMinor != null) {
            query.setJvmBinaryMinor(jvmBinaryMinor);
        }
        if (jsBinaryMajor != null) {
            query.setJsBinaryMajor(jsBinaryMajor);
        }
        if (jsBinaryMinor != null) {
            query.setJsBinaryMinor(jsBinaryMinor);
        }
        return query;
    }

    protected Collection<ModuleVersionDetails> getModuleVersions(String namespace, String name, String version, boolean exactVersionMatch,
            ModuleQuery.Type type, 
    		Integer jvmBinaryMajor, Integer jvmBinaryMinor,
    		Integer jsBinaryMajor, Integer jsBinaryMinor) {
        return getModuleVersions(getRepositoryManager(), namespace, name, version, exactVersionMatch, type, 
        		jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor);
    }

    protected Collection<ModuleVersionDetails> getModuleVersions(RepositoryManager repoMgr, String namespace, String name, String version, 
            boolean exactVersionMatch, ModuleQuery.Type type, 
    		Integer jvmBinaryMajor, Integer jvmBinaryMinor,
    		Integer jsBinaryMajor, Integer jsBinaryMinor) {
        ModuleVersionQuery query = getModuleVersionQuery(namespace, name, version, type, 
        		jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor);
        query.setExactVersionMatch(exactVersionMatch);
        ModuleVersionResult result = repoMgr.completeVersions(query);
        NavigableMap<String, ModuleVersionDetails> versionMap = result.getVersions();
        return versionMap.values();
    }

    protected String checkModuleVersionsOrShowSuggestions(String name, String version, 
    		ModuleQuery.Type type, 
    		Integer jvmBinaryMajor, Integer jvmBinaryMinor,
    		Integer jsBinaryMajor, Integer jsBinaryMinor) throws IOException {
        return checkModuleVersionsOrShowSuggestions(name, version, type, 
        		jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor, COMPILE_NEVER);
    }
    
    protected static final String COMPILE_NEVER = "never";
    protected static final String COMPILE_ONCE = "once";
    protected static final String COMPILE_CHECK = "check";
    protected static final String COMPILE_FORCE = "force";
    
    protected String processCompileFlags(String compileFlags, String configFlags) {
        if (compileFlags == null) {
            compileFlags = configFlags;
            if (compileFlags.isEmpty() || !validCompileFlags(compileFlags)) {
                compileFlags = COMPILE_NEVER;
            }
        } else if (compileFlags.isEmpty()) {
            compileFlags = COMPILE_CHECK;
        } else if (!validCompileFlags(compileFlags)) { 
            throw new IllegalArgumentException("Invalid compile flag '" + compileFlags + "', should be one of: once, check, force, never");
        }
        
        return compileFlags;
    }
    
    private boolean validCompileFlags(String compileFlags) {
        return (COMPILE_NEVER.equals(compileFlags) || COMPILE_ONCE.equals(compileFlags)
                || COMPILE_CHECK.equals(compileFlags) || COMPILE_FORCE.equals(compileFlags));
    }

    protected String checkModuleVersionsOrShowSuggestions(String name, String version, 
    		ModuleQuery.Type type, 
    		Integer jvmBinaryMajor, Integer jvmBinaryMinor,
    		Integer jsBinaryMajor, Integer jsBinaryMinor, 
    		String compileFlags) throws IOException {
        RepositoryManager repoMgr = getRepositoryManager();
        if (compileFlags == null || compileFlags.isEmpty() || !compilationPossible()) {
            compileFlags = COMPILE_NEVER;
        }
        boolean forceCompilation = compileFlags.contains(COMPILE_FORCE);
        boolean checkCompilation = compileFlags.contains(COMPILE_CHECK);
        boolean allowCompilation = forceCompilation || checkCompilation || compileFlags.contains(COMPILE_ONCE);
        
        Collection<ModuleVersionDetails> versions = null;
        
        if (ModuleUtil.isDefaultModule(name) || version != null) {
            // If we have the default module or a version we first try it the quick way
            ArtifactContext ac = new ArtifactContext(null, name, version, type.getSuffixes());
            ac.setIgnoreDependencies(true);
            ac.setThrowErrorIfMissing(false);
            ArtifactResult result = repoMgr.getArtifactResult(ac);
            if (result != null) {
                if (forceCompilation || checkCompilation) {
                    String v = result.version() != null ? result.version() : "unversioned";
                    versions = Collections.singletonList(new ModuleVersionDetails(name, v, result.groupId(), result.groupId()));
                } else {
                    return (result.version() != null) ? result.version() : "";
                }
            } else if (ModuleUtil.isDefaultModule(name) && !allowCompilation) {
                String err = getModuleNotFoundErrorMessage(repoMgr, name, version);
                throw new ToolUsageError(err);
            }
        }

        boolean suggested = false;
        
        // finding a single compiled version in the output repo is a lot cheaper than query everything so let's
        // try that first
        if (version == null && !ModuleUtil.isDefaultModule(name) && versions == null) {
            versions = findCompiledVersions(getOfflineRepositoryManager(), name, type, 
            		jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor);
            if (versions != null && versions.size() == 1) {
                ModuleVersionDetails compiledVersion = versions.iterator().next();
                if (compiledVersion != null && compiledVersion.getVersion() != null) {
                    if (forceCompilation || checkCompilation) {
                        versions = Collections.singleton(compiledVersion);
                    } else {
                        return compiledVersion.getVersion();
                    }
                }
            }
        }

        // if we did not find any version in the output repo, see if we have a single one in the source repo, that's
        // a lot cheaper than looking the version up
        ModuleVersionDetails srcVersion = null;
        if (allowCompilation || (versions != null && versions.size() > 1)) {
            srcVersion = getModuleVersionDetailsFromSource(name);
            if (srcVersion != null && version == null) {
                if (versions == null) {
                    // we found some source and no local compiled versions exist,
                    // let's compile it and not even look up anything else
                    versions = Collections.emptyList();
                } else {
                    // we found some source and several local compiled versions exist,
                    // let's if one of them matches and use that one
                    for (ModuleVersionDetails mvd : versions) {
                        if (sameVersion(name, mvd.getVersion(), srcVersion.getVersion())) {
                            if (forceCompilation || checkCompilation) {
                                versions = Collections.singleton(mvd);
                            } else {
                                return mvd.getVersion();
                            }
                        }
                    }
                }
            }
        }
        
        // find versions unless we have one in sources waiting to be compiled
        if (versions == null) {
            // First see which versions we have available locally
            versions = getModuleVersions(getOfflineRepositoryManager(), null, name, version, false, type, 
            		jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor);
            if (versions.isEmpty() && !offline) {
                // No local versions and we're not offline, so let's try again online
                versions = getModuleVersions(repoMgr, null, name, version, false, type, 
                        jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor);
            }
            if (version != null && !versions.isEmpty()) {
                // We have one or more matching versions, let's see if one is exactly the same
                // while not having any partial matches
                boolean partialMatch = false;
                ModuleVersionDetails exactMatch = null;
                for (ModuleVersionDetails v : versions) {
                    if (version.equals(v.getVersion())) {
                        exactMatch = v;
                    } else if (v.getVersion().startsWith(version)) {
                        partialMatch = true;
                    }
                }
                if (exactMatch != null && !partialMatch) {
                    versions = Collections.singletonList(exactMatch);
                }
            }
        }
        
        if (version != null && (versions.isEmpty() || exactSingleMatch(versions, version))) {
            // Here we either have a single version or none
            if (versions.isEmpty() || forceCompilation || (checkCompilation && shouldRecompile(getOfflineRepositoryManager(), name, version, type, true))) {
                if (allowCompilation) {
                    if (srcVersion != null) {
                        if (version.equals(srcVersion.getVersion())) {
                            // There seems to be source code that has the proper version
                            // Let's see if we can compile it...
                            if (!runCompiler(repoMgr, name, type, compileFlags)) {
                                throw new ToolUsageError(Messages.msg(bundle, "compilation.failed"));
                            }
                        } else {
                            suggested = true;
                        }
                        // All okay it seems, let's use this version
                        versions = Arrays.asList(srcVersion);
                    }
                }
                if (versions.isEmpty()) {
                    // Maybe the user specified the wrong version?
                    // Let's see if we can find any locally and suggest them
                    versions = getModuleVersions(getOfflineRepositoryManager(), null, name, null, false, type,
                    		jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor);
                    if (versions.isEmpty() && !offline) {
                        // No local versions and we're not offline, so let's try again online
                        versions = getModuleVersions(repoMgr, null, name, null, false, type,
                                jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor);
                    }
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
                if ((srcVersion != null || ModuleUtil.isDefaultModule(name))
                        && (version == null || version.equals(srcVersion.getVersion()))) {
                    // There seems to be source code
                    // Let's see if we can compile it...
                    String srcver = ModuleUtil.isDefaultModule(name) ? null : srcVersion.getVersion();
                    if (!checkCompilation || shouldRecompile(getOfflineRepositoryManager(), name, srcver, type, true)) {
                        if (!runCompiler(repoMgr, name, type, compileFlags)) {
                            throw new ToolUsageError(Messages.msg(bundle, "compilation.failed"));
                        }
                    }
                    // All okay it seems, let's use this version
                    versions = Arrays.asList(srcVersion);
                }
            }
        }
        if (versions.isEmpty()) {
            String err = getModuleNotFoundErrorMessage(repoMgr, name, version);
            throw new ToolUsageError(err);
        }
        if (versions.size() > 1 || inexactSingleMatch(versions, version) || suggested) {
            StringBuilder err = new StringBuilder();
            if (version == null) {
                err.append(Messages.msg(bundle, "missing.version", name));
            } else {
                err.append(Messages.msg(bundle, "version.not.found", version, name));
            }
            err.append("\n");
            err.append(Messages.msg(bundle, "try.versions"));
            boolean first = true;
            for (ModuleVersionDetails mvd : versions) {
                if (!first) {
                    err.append(", ");
                }
                err.append(mvd.getVersion());
                if (mvd.isRemote()) {
                    err.append(" (*)");
                }
                first = false;
            }
            err.append("\n");
            throw new ToolUsageError(err.toString());
        }
        if (ModuleUtil.isDefaultModule(name)) {
            return "";
        } else {
            return versions.iterator().next().getVersion();
        }
    }
    
    private boolean sameVersion(String name, String version1, String version2) {
        if (ModuleUtil.isDefaultModule(name)) {
            return "unversioned".equals(version1) && "unversioned".equals(version2);
        } else {
            return version1 != null && version2 != null && version1.equals(version2);
        }
    }

    private boolean exactSingleMatch(Collection<ModuleVersionDetails> versions, String version) {
        return version != null && (versions.size() == 1) && version.equals(versions.iterator().next().getVersion());
    }
    
    private boolean inexactSingleMatch(Collection<ModuleVersionDetails> versions, String version) {
        return version != null && (versions.size() == 1) && !version.equals(versions.iterator().next().getVersion());
    }
    
    private Collection<ModuleVersionDetails> findCompiledVersions(RepositoryManager repoMgr,
            String name, 
    		Type type, 
    		Integer jvmBinaryMajor, Integer jvmBinaryMinor,
    		Integer jsBinaryMajor, Integer jsBinaryMinor) throws IOException {
        String outRepo = DefaultToolOptions.getCompilerOutputRepo();
        if(outRepo != null){
            File outDir = new File(outRepo);
            CmrRepository rep = null;
            List<CmrRepository> repositories = repoMgr.getRepositories();
            for(CmrRepository repository : repositories){
                OpenNode root = repository.getRoot();
                // it has binaries if it is not a folder
                if(root.isRemote() || root.hasBinaries())
                    continue;
                ContentStore service = root.getService(ContentStore.class);
                if(service == null)
                    continue;
                ContentHandle content = service.peekContent(root);
                // again skip binaries
                if(content == null || content.hasBinaries())
                    continue;
                File repoFile = content.getContentAsFile();
                if(repoFile != null && FileUtil.sameFile(repoFile, outDir)){
                    rep = repository;
                    break;
                }
            }
            if(rep != null && rep.isSearchable()){
                ModuleVersionQuery query = getModuleVersionQuery(null, name, null, type, 
                		jvmBinaryMajor, jvmBinaryMinor, jsBinaryMajor, jsBinaryMinor);
                ModuleVersionResult result = new ModuleVersionResult(query.getName());
                rep.completeVersions(query, result);
                NavigableMap<String, ModuleVersionDetails> outRepoVersions = result.getVersions();
                if (!outRepoVersions.isEmpty()) {
                    return outRepoVersions.values();
                }
            }
        }
        return null;
    }

    protected String getModuleNotFoundErrorMessage(RepositoryManager repoMgr, String name, String version) {
        return getModuleNotFoundErrorMessage(repoMgr, name, version, "module.not.found");
    }

    protected String getModuleNotFoundErrorMessage(RepositoryManager repoMgr, String name, String version, String msgkeyNotFound) {
        StringBuilder err = new StringBuilder();
        String descr = version == null ? name : name+"/"+version;
        err.append(Messages.msg(bundle, msgkeyNotFound, descr));
        err.append("\n");
        boolean fullySearchable = true;
        for (CmrRepository repo : repoMgr.getRepositories()) {
            if (version != null || repo.isSearchable()) {
                err.append("    ");
                err.append(repo.getDisplayString());
                err.append("\n");
            } else {
                fullySearchable = false;
            }
        }
        if (version == null && !fullySearchable) {
            err.append(Messages.msg(bundle, "missing.version.suggestion"));
        }
        return err.toString();
    }

    protected boolean shouldRecompile(RepositoryManager repoMgr, String name, String version, ModuleQuery.Type type, boolean checkTime) throws IOException {
        ArtifactResult art = getModuleArtifact(repoMgr, name, version, type);
        if (art == null) {
            return true;
        }
        File artifile = art.artifact();
        // Check if it has META-INF/errors.txt
        Properties errors = getMetaInfErrors(artifile);
        if (errors != null && !errors.isEmpty()) {
            return true;
        }
        if (checkTime) {
            return isModuleArtifactOutOfDate(artifile, name, type);
        }
        return false;
    }

    protected ArtifactResult getModuleArtifact(RepositoryManager repoMgr, String name, String version, ModuleQuery.Type type) {
        ArtifactContext ac = new ArtifactContext(null, name, version, type.getSuffixes());
        ac.setIgnoreDependencies(true);
        ac.setThrowErrorIfMissing(false);
        return repoMgr.getArtifactResult(ac);
    }

    protected Properties getMetaInfErrors(File carFile) {
        try {
            return JarUtils.getMetaInfProperties(carFile, "META-INF/errors.txt");
        } catch (IOException e) {
            return null;
        }
    }
    
    protected Properties getMetaInfHashes(File carFile) {
        try {
            return JarUtils.getMetaInfProperties(carFile, "META-INF/hashes.txt");
        } catch (IOException e) {
            return null;
        }
    }

    protected boolean isModuleArtifactOutOfDate(File artifile, String name, ModuleQuery.Type type) throws IOException {
        long oldestArtifact;
        if (type == ModuleQuery.Type.JVM) {
            oldestArtifact = JarUtils.oldestFileTime(artifile);
        } else {
            oldestArtifact = artifile.lastModified();
        }
        long newestSource = getNewestLastmodified(name);
        return (newestSource > oldestArtifact);
    }

    private long getNewestLastmodified(String name) throws IOException {
        final long[] newest = new long[] { -1L };
        List<File> dirs = allDirs();
        for (File dir : dirs) {
            File moduleDir = ModuleUtil.moduleToPath(dir, name);
            if (moduleDir.isDirectory() && moduleDir.canRead()) {
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
    
    protected ModuleVersionReader getModuleVersionReader() {
        if (mvr == null) {
            mvr = new ModuleVersionReader(getSourceDirs()).cwd(getCwd());
        }
        return mvr;
    }
    
    protected ModuleVersionDetails getModuleVersionDetailsFromSource(String moduleName) {
        return getModuleVersionReader().fromSource(moduleName);
    }

    /**
     * Override in subclasses that accept source dirs
     * @return
     */
    protected List<File> getSourceDirs() {
        Type compilerType = getCompilerType();
        if(compilerType == null)
            return DefaultToolOptions.getCompilerSourceDirs();
        return getSourceDirsForCompiler(compilerType);
    }
    
    /**
     * Override in subclasses that accept compilation before run, this is used
     * to determine the compiler arguments.
     * @return the compiler backend, or null to use default compiler arguments
     */
    protected ModuleQuery.Type getCompilerType(){
        return null;
    }
    
    /**
     * Use in subclasses that accept compilation before run
     */
    protected List<File> getSourceDirsForCompiler(ModuleQuery.Type type){
        List<String> args = new ArrayList<String>(compilerArguments.size()+1);
        args.addAll(compilerArguments);
            
        ToolFactory pluginFactory = new ToolFactory();
        ToolLoader pluginLoader = new CeylonToolLoader();
        String toolName;
        if (type == ModuleQuery.Type.JVM) {
            toolName = "compile";
        } else if (type == ModuleQuery.Type.JS) {
            toolName = "compile-js";
        } else if (type == ModuleQuery.Type.DART) {
            toolName = "compile-dart";
        } else {
            throw new IllegalArgumentException("Unknown compile flags passed");
        }
        try {
            ToolModel<Tool> model = pluginLoader.loadToolModel(toolName);
            Tool tool = pluginFactory.bindArguments(model, pluginLoader.<CeylonTool>instance("", null), args);
            if(tool instanceof RepoUsingTool)
                return ((RepoUsingTool)tool).getSourceDirs();
        } catch (NonFatalToolMessage m) {
            m.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DefaultToolOptions.getCompilerSourceDirs();
    }
    
    /**
     * Override in subclasses that accept source dirs
     * @return
     */
    protected List<File> getResourceDirs() {
        return DefaultToolOptions.getCompilerResourceDirs();
    }

    protected List<File> allDirs() {
        List<File> all = new ArrayList<File>(getSourceDirs().size() + getResourceDirs().size());
        all.addAll(getSourceDirs());
        all.addAll(getResourceDirs());
        return applyCwd(all);
    }

    protected boolean isSourceModule(String name, String version, List<File> srcDirs){
        for (File srcDir : srcDirs) {
            if(isSourceModule(srcDir, name, version))
                return true;
        }
        return false;
    }

    private boolean isSourceModule(File srcDir, String name, String version) {
        try{
            ModuleDescriptorReader mdr = new ModuleDescriptorReader(name, srcDir);
            String declaredVersion = mdr.getModuleVersion();
            return name.equals("default") || version.equals(declaredVersion);
        }catch(ModuleDescriptorReader.NoSuchModuleException x){
            return false;
        }
    }

    protected List<ModuleSpec> getSourceModules(List<File> srcDirs){
        Map<String, ModuleSpec> modules = new HashMap<String, ModuleSpec>();
        for (File srcDir : srcDirs) {
            collectModules(srcDir, srcDir, modules);
        }
        ArrayList<ModuleSpec> ret = new ArrayList<ModuleSpec>(modules.size());
        ret.addAll(modules.values());
        return ret;
    }
    
    private void collectModules(File sourceRoot, File sourceFile, Map<String, ModuleSpec> modules) {
        if(sourceFile.isDirectory()){
            File moduleFile = new File(sourceFile, Constants.MODULE_DESCRIPTOR);
            // do we have a module file?
            if(moduleFile.exists()){
                collectModule(sourceRoot, sourceFile, modules);
                // we done with this tree
                return;
            }
            // recurse down normal folders
            for(File f : sourceFile.listFiles()){
                collectModules(sourceRoot, f, modules);
            }
        }else{
            String name = sourceFile.getName().toLowerCase();
            // did we find a source file?
            if(name.endsWith(".ceylon")
                    || name.endsWith(".java")
                    || name.endsWith(".js")){
                // we have a default module
                if(!modules.containsKey("default"))
                    modules.put("default", ModuleSpec.DEFAULT_MODULE);
            }
        }
    }

    private void collectModule(File sourceRoot, File sourceFile, Map<String, ModuleSpec> modules) {
        File relativeFile = FileUtil.relativeFile(sourceRoot, sourceFile);
        String name = relativeFile.getPath().replace(File.separatorChar, '.');
        if(modules.containsKey(name))
            return;
        try{
            ModuleDescriptorReader mdr = new ModuleDescriptorReader(name, sourceRoot);
            String version = mdr.getModuleVersion();
            if (version != null) {
                modules.put(name, new ModuleSpec(null, name, version));
            }
        }catch(ModuleDescriptorReader.NoSuchModuleException x){
            // skip this source folder and look in the next one
            x.printStackTrace();
        }
    }

    // We only compile in "normal" circumstances, meaning that
    // either no repositories are specified or that the default
    // output repository (normally "modules") is one of them.
    // We also compile if specific compiler arguments have been set
    protected boolean compilationPossible() {
        if (compilerArguments == null && repos != null) {
            List<File> files = FileUtil.pathsToFileList(getRepositoryAsStrings());
            File out = new File(DefaultToolOptions.getCompilerOutputRepo(), "dummy");
            File path = FileUtil.selectPath(files, out.getPath());
            return path != null;
        } else {
            return true;
        }
    }
    
    protected List<String> getCompilerArguments() {
        List<String> args = new ArrayList<String>();
        // user-specified overrides every automatic arg
        if(compilerArguments != null){
            args.addAll(compilerArguments);
            return args;
        }
        if (noDefRepos) {
            args.add("--no-default-repositories");
        }
        if (systemRepo != null) {
            args.add("--sysrep");
            args.add(systemRepo);
        }
        if (cacheRepo != null) {
            args.add("--cacherep");
            args.add(cacheRepo);
        }
        if (repos != null) {
            for (URI r : repos) {
                args.add("--rep");
                args.add(r.toString());
            }
        }
        if (offline) {
            args.add("--offline");
        }
        if (verbose != null) {
            args.add("--verbose=" + verbose);
        }
        return args;
    }

    private boolean runCompiler(RepositoryManager repoMgr, String name, ModuleQuery.Type type, String compileFlags) {
        List<String> args = getCompilerArguments();
        if (compileFlags != null) {
            args.add("--include-dependencies=" + compileFlags);
        }
        if (!compileFlags.contains(COMPILE_FORCE)
                && type == ModuleQuery.Type.JVM) {
            args.add("--incremental");
        }
        args.add(name);
        
        ToolFactory pluginFactory = new ToolFactory();
        ToolLoader pluginLoader = new CeylonToolLoader();
        String toolName;
        if (type == ModuleQuery.Type.JVM) {
            toolName = "compile";
        } else if (type == ModuleQuery.Type.JS) {
            toolName = "compile-js";
        } else if (type == ModuleQuery.Type.DART) {
            toolName = "compile-dart";
        } else {
            throw new IllegalArgumentException("Unknown compile flags passed");
        }
        try {
            ToolModel<Tool> model = pluginLoader.loadToolModel(toolName);
            Tool tool = pluginFactory.bindArguments(model, pluginLoader.<CeylonTool>instance("", null), args);
            msg("compiling", name).newline();
            tool.run();
            // Make sure we can find the newly created module
            repoMgr.refresh(false);
            if (repoMgr == rm) {
                rm = null;
            }
        } catch (NonFatalToolMessage m) {
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
    
    public RepoUsingTool errorAppend(Object s) throws IOException {
        return append(error, s);
    }
    
    public RepoUsingTool newline(Appendable out) throws IOException {
        out.append(System.lineSeparator());
        return this;
    }
    
    public RepoUsingTool newline() throws IOException {
        return newline(out);
    }
    
    public RepoUsingTool errorNewline() throws IOException {
        return newline(error);
    }
    
    public RepoUsingTool flush() throws IOException {
        if (out instanceof Flushable) {
            ((Flushable)out).flush();
        }
        if (error instanceof Flushable) {
            ((Flushable)error).flush();
        }
        return this;
    }
    
    public Appendable getOutAppendable(){
        return out;
    }
    
    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        super.initialize(mainTool);
        if (overrides != null) {
            File of = FileUtil.applyCwd(cwd, new File(overrides));
            if (!of.exists()) {
                throw new IllegalArgumentException("Overrides file '"+of+"' does not exist");
            }
        }
    }
}

