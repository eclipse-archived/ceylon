package com.redhat.ceylon.cmr.ceylon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactCreator;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.impl.CMRJULLogger;
import com.redhat.ceylon.cmr.impl.CachingRepositoryManager;
import com.redhat.ceylon.cmr.impl.FileContentStore;
import com.redhat.ceylon.cmr.impl.ResourceArtifactCreatorImpl;
import com.redhat.ceylon.cmr.impl.SimpleRepositoryManager;
import com.redhat.ceylon.cmr.impl.SourceArtifactCreatorImpl;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.config.Repositories;
import com.redhat.ceylon.common.log.Logger;

public class CeylonUtils {

    public static CeylonRepoManagerBuilder repoManager() {
        return new CeylonRepoManagerBuilder();
    }

    public static class CeylonRepoManagerBuilder {
        private CeylonConfig config;
        private File actualCwd;
        private File cwd;
        private String systemRepo;
        private String cacheRepo;
        private String overrides;
        private List<String> userRepos;
        private List<String> extraUserRepos;
        private List<String> remoteRepos;
        private String outRepo;
        private String user;
        private String password;
        private int timeout = -1;
        private boolean offline;
        private boolean noSystemRepo;
        private boolean noDefRepos;
        private boolean jdkIncluded;
        private Logger log;
        private String avoidRepository;

        /**
         * Sets the configuration to use for building the repository manager
         *
         * @param config A reference to a CeylonConfig object
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder config(CeylonConfig config) {
            this.config = config;
            return this;
        }

        /**
         * Sets the current working directory to use for building the repository manager.
         * This is used to encounter the configuration file when <code>config</code> isn't set
         * and also functions as the base folder for any relative paths that might be encountered.
         * When not set the current directory as defined by <code>new File(".")</code> will be used
         *
         * @param cwd A File referencing a folder that could possibly contain a <code>.ceylon</code>
         *            sub-folder with a <code>config</code> file
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder cwd(File cwd) {
            this.cwd = cwd;
            return this;
        }

        /**
         * Sets the current working directory to use for building the repository manager.
         * This is used to encounter the configuration file when <code>config</code> isn't set
         * and also functions as the base folder for any relative paths that might be encountered.
         * When not set the current directory as defined by <code>new File(".")</code> will be used
         *
         * @param cwd A String containing a folder path that could possibly contain
         *            a <code>.ceylon</code> sub-folder with a <code>config</code> file
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder cwd(String cwd) {
            if (cwd != null) {
                this.cwd = new File(cwd);
            } else {
                this.cwd = null;
            }
            return this;
        }

        /**
         * Sets the path to use for the system repository where the essential Ceylon
         * modules (compiler, language, spec, etc) can be found. When not set the
         * value will be taken from the system configuration
         *
         * @param systemRepo A path to a Ceylon repository
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder systemRepo(String systemRepo) {
            this.systemRepo = systemRepo;
            return this;
        }

        /**
         * Make sure we never try to read from the given repository. This is mostly
         * useful if you intend to write to it and want to make sure we never read
         * from it at the same time.
         *
         * @param avoidRepo A path to a Ceylon repository
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder avoidRepo(String avoidRepo) {
            this.avoidRepository = avoidRepo;
            return this;
        }

        /**
         * Sets the path to use for the caching of downloaded modules. When not set the
         * value will be taken from the system configuration
         *
         * @param cacheRepo A path to a folder to use for caching
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder cacheRepo(String cacheRepo) {
            this.cacheRepo = cacheRepo;
            return this;
        }

        /**
         * Sets the path to use for the module overrides XML file
         *
         * @param overrides the path to use for the module overrides XML file
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder overrides(String overrides) {
            this.overrides = overrides;
            return this;
        }

        /**
         * Indicates that we don't need the default system repository (defaults to false)
         */
        public CeylonRepoManagerBuilder noSystemRepo(boolean noSystemRepo){
            this.noSystemRepo = noSystemRepo;
            return this;
        }

        /**
         * Indicates that the default repositories should not be used (defaults to false)
         */
        public CeylonRepoManagerBuilder noDefaultRepos(boolean noDefRepos){
            this.noDefRepos = noDefRepos;
            return this;
        }
        
        /**
         * Sets a list of paths to use for the remote repositories. When not set the
         * list will be taken from the system configuration (they don't really have
         * to be remote, but they're called that because they will be last in the list
         * where you'd normally put remote repositories)
         *
         * @param remoteRepos A list of paths to Ceylon repositories
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder remoteRepos(List<String> remoteRepos) {
            this.remoteRepos = remoteRepos;
            return this;
        }

        /**
         * Sets a list of paths to use for the user repositories. When not set the
         * list will be taken from the system configuration. When set this list
         * will override any "local" repositories that might have been defined
         * in Ceylon's configuration files
         *
         * @param userRepos A list of paths to Ceylon repositories
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder userRepos(List<String> userRepos) {
            this.userRepos = userRepos;
            return this;
        }

        /**
         * Sets a list of paths to use for the user repositories. The difference with
         * `userRepos` is that providing this list has no side-effects, it will not
         * override anything nor has it any (configured) default value
         *
         * @param extraUserRepos A list of paths to Ceylon repositories
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder extraUserRepos(List<String> extraUserRepos) {
            this.extraUserRepos = extraUserRepos;
            return this;
        }

        /**
         * Sets the path to use for the output repository. For a normal manager
         * (@see #buildManager()) this is used only to look up modules,
         * for writing the output manager is needed (@see #buildOutputManager()).
         * When not set the value will be taken from the system configuration
         *
         * @param outRepo A path to a Ceylon repository
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder outRepo(String outRepo) {
            this.outRepo = outRepo;
            return this;
        }
        
        /**
         * Returns the output repository after expansion has taken place in buildOutputManager(). Only
         * call this after buildOutputManager has been called.
         */
        public String getResolvedOutRepo(){
            return outRepo;
        }

        /**
         * Sets the user name to use for writing to the output repository.
         * When not set the value will be take from the system configuration (if available)
         *
         * @param user A user name
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder user(String user) {
            this.user = user;
            return this;
        }

        /**
         * Sets the password to use for writing to the output repository.
         * This password is not encrypted in any way!
         * When not set the value will be take from the system configuration (if available)
         *
         * @param password A password
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        /**
         * Sets the timeout in milliseconds for any connections that might be established
         * to remote repositories (defaults to 20000)
         */
        public CeylonRepoManagerBuilder timeout(int timeout){
            this.timeout = timeout;
            return this;
        }
        
        /**
         * Enables offline mode that will prevent the module loader from connecting
         * to remote repositories (defaults to false)
         */
        public CeylonRepoManagerBuilder offline(boolean offline){
            this.offline = offline;
            return this;
        }
        
        /**
         * Set to true to have JDK modules included in search/completion queries
         */
        public CeylonRepoManagerBuilder isJDKIncluded(boolean include){
            this.jdkIncluded = include;
            return this;
        }

        /**
         * The logger to use, both for the builder itself as well as the
         * manager under construction. When not set <code>URLLogger</code> will be used
         *
         * @param logger A logger
         * @return This object for chaining method calls
         */
        public CeylonRepoManagerBuilder logger(Logger logger) {
            this.log = logger;
            return this;
        }

        /**
         * Creates a RepositoryManager used for doing lookups using the current state of this builder
         *
         * @return A new RepositoryManager
         */
        public RepositoryManager buildManager() {
            return buildManagerBuilder().buildRepository();
        }

        /**
         * Creates a RepositoryManagerBuilder used for doing lookups using the current state of <b>this</b> builder
         *
         * @return A new RepositoryManagerBuilder
         */
        public RepositoryManagerBuilder buildManagerBuilder() {
            if (log == null) {
                log = new CMRJULLogger();
            }

            // Make sure we have a configuration

            actualCwd = new File(".");
            if (config == null) {
                // No configuration passed, lets get/load one
                if (cwd == null || actualCwd.equals(cwd)) {
                    cwd = actualCwd;
                    config = CeylonConfig.get();
                } else {
                    config = CeylonConfig.createFromLocalDir(cwd);
                }
            } else if (cwd == null) {
                cwd = actualCwd;
            }
            
            // Access all the repository information from the configuration
            
            Repositories repositories = Repositories.withConfig(config);

            // First we determine the cache repository that's needed to create CMR's RepositoryManagerBuilder

            File root;
            if (cacheRepo == null) {
                Repositories.Repository cr = repositories.getCacheRepository();
                root = new File(absolute(cr.getUrl()));
            } else {
                root = new File(absolute(resolveRepoUrl(repositories, cacheRepo)));
            }
            // do not use the cache if we want to avoid its repo
            // For example, if we intend to write to it, we should never read from it
            if(avoidRepository(root.getAbsolutePath()))
                root = null;

            final RepositoryManagerBuilder builder = new RepositoryManagerBuilder(root, log, isOffline(config), getTimeout(config), getOverrides(config));

            // Now we add all the rest of the repositories in the order that they will be searched
            
            if (systemRepo == null) {
                if(!noSystemRepo){
                    addRepo(builder, repositories.getSystemRepository());
                }
            } else {
                addRepo(builder, repositories, systemRepo);
            }

            if (outRepo == null) {
                // do not add the output repo if we do not specify it and do not want the default repos
                if(!noDefRepos){
                    addRepo(builder, repositories.getOutputRepository());
                }
            } else {
                addRepo(builder, repositories, outRepo);
            }

            if (jdkIncluded) {
                addRepo(builder, repositories, "jdk");
            }

            if (userRepos != null && !userRepos.isEmpty()) {
                // Add user defined repos
                for (String repo : userRepos) {
                    addRepo(builder, repositories, repo);
                }
            } else {
                // We add the configured local lookup repos only when no user defined repos have been passed
                if (!noDefRepos) {
                    Repositories.Repository[] repos = repositories.getLocalLookupRepositories();
                    for (Repositories.Repository lookup : repos) {
                        addRepo(builder, lookup);
                    }
                }
            }

            // Add the extra user defined repos
            if (extraUserRepos != null && !extraUserRepos.isEmpty()) {
                for (String repo : extraUserRepos) {
                    addRepo(builder, repositories, repo);
                }
            }
                
            // Add default non-remote repos (like the user repo)
            if (!noDefRepos) {
                Repositories.Repository[] globals = repositories.getGlobalLookupRepositories();
                for (Repositories.Repository lookup : globals) {
                    addRepo(builder, lookup);
                }
            }

            // Add the "remote" repos (not necessarily remote but it's at the point in the
            // lookup order where you'd normally define remote repos)
            if (remoteRepos != null && !remoteRepos.isEmpty()) {
                // Add remote repos
                for (String repo : remoteRepos) {
                    addRepo(builder, repositories, repo);
                }
            } else {
                // We add the configured remote lookup repos only when no user defined repos have been passed
                if (!noDefRepos) {
                    Repositories.Repository[] repos = repositories.getRemoteLookupRepositories();
                    for (Repositories.Repository lookup : repos) {
                        addRepo(builder, lookup);
                    }
                }
            }

            // Add the remaining ("other") default repos (like the Herd repo),
            // these will always come last
            if (!noDefRepos) {
                Repositories.Repository[] others = repositories.getOtherLookupRepositories();
                for (Repositories.Repository lookup : others) {
                    addRepo(builder, lookup);
                }
                // finally add the Aether repo if we don't have one already
                if(!builder.hasMavenRepository() && !avoidRepository("aether:")){
                    try {
                        Repository repo = builder.repositoryBuilder().buildRepository("aether:");
                        builder.addRepository(repo);
                    } catch (Exception e) {
                        log.debug("Failed to add Maven (aether) repository as input repository: " + e.getMessage());
                    }
                }
            }

            log.debug("Repository lookup order:");
            for (String rds : builder.getRepositoriesDisplayString()) {
                log.debug(" - " + rds);
            }

            return builder;
        }

        /**
         * Creates a RepositoryManager used for writing using the current state of this builder
         *
         * @return A new RepositoryManager
         */
        public RepositoryManager buildOutputManager() {
            if (log == null) {
                log = new CMRJULLogger();
            }

            // Make sure we have a configuration

            actualCwd = new File(".");
            if (config == null) {
                // No configuration passed, lets get/load one
                if (cwd == null || actualCwd.equals(cwd)) {
                    cwd = actualCwd;
                    config = CeylonConfig.get();
                } else {
                    config = CeylonConfig.createFromLocalDir(cwd);
                }
            } else if (cwd == null) {
                cwd = actualCwd;
            }

            // Access all the repository information from the configuration
            
            Repositories repositories = Repositories.withConfig(config);

            if (outRepo == null) {
                Repositories.Repository repo = repositories.getOutputRepository();
                outRepo = repo.getUrl();
                if (user == null && repo.getCredentials() != null) {
                    user = repo.getCredentials().getUser();
                }
                if (password == null && repo.getCredentials() != null) {
                    password = repo.getCredentials().getPassword();
                }
            } else {
                if (outRepo.startsWith("+")) {
                    // The token is the name of a repository defined in the Ceylon configuration file
                    Repositories.Repository repo = repositories.getRepository(outRepo.substring(1));
                    if (repo != null) {
                        outRepo = repo.getUrl();
                    }
                }
            }

            final String key = (outRepo.startsWith("${") ? outRepo.substring(2, outRepo.length() - 1) : outRepo);
            final String temp = System.getProperty(key);
            if (temp != null) {
                outRepo = temp;
            }

            if (!isHttp(outRepo)) {
                outRepo = absolute(outRepo);
                File repoFolder = new File(outRepo);
                if (repoFolder.exists()) {
                    if (!repoFolder.isDirectory()) {
                        log.error("Output repository is not a directory: " + outRepo);
                    } else if (!repoFolder.canWrite()) {
                        log.error("Output repository is not writable: " + outRepo);
                    }
                } else if (!repoFolder.mkdirs()) {
                    log.error("Failed to create output repository: " + outRepo);
                }
                StructureBuilder structureBuilder = new FileContentStore(repoFolder);
                return new SimpleRepositoryManager(structureBuilder, log);
            } else {
                File cachingDir = FileUtil.makeTempDir("ceylon-webdav-cache-");

                // HTTP
                WebDAVContentStore davContentStore = new WebDAVContentStore(outRepo, log, false, getTimeout(config));
                davContentStore.setUsername(user);
                davContentStore.setPassword(password);

                return new CachingRepositoryManager(davContentStore, cachingDir, log);
            }
        }

        private void addRepo(RepositoryManagerBuilder builder, Repositories.Repository repoInfo) {
            if (repoInfo != null) {
                try {
                    String path = absolute(repoInfo.getUrl());
                    if(!avoidRepository(path)){
                        Repository repo = builder.repositoryBuilder().buildRepository(path);
                        builder.addRepository(repo);
                    }
                } catch (Exception e) {
                    log.debug("Failed to add repository as input repository: " + repoInfo.getUrl() + ": " + e.getMessage());
                }
            }
        }

        private boolean avoidRepository(String path) {
            return avoidRepository != null && avoidRepository.equals(path);
        }

        private String resolveRepoUrl(Repositories repositories, String repoUrl) {
            if (repoUrl.startsWith("+")) {
                // The token is the name of a repository defined in the Ceylon configuration file
                String path = absolute(repoUrl.substring(1));
                Repositories.Repository repo = repositories.getRepository(path);
                if (repo != null) {
                    repoUrl = repo.getUrl();
                }
            }
            return repoUrl;
        }
        
        private void addRepo(RepositoryManagerBuilder builder, Repositories repositories, String repoUrl) {
            try {
                if (repoUrl.startsWith("+")) {
                    // The token is the name of a repository defined in the Ceylon configuration file
                    String path = repoUrl.substring(1);
                    Repositories.Repository repo = repositories.getRepository(path);
                    if (repo != null) {
                        repoUrl = repo.getUrl();
                    }
                }
                String path = absolute(repoUrl);
                if(!avoidRepository(path)){
                    Repository repo = builder.repositoryBuilder().buildRepository(path);
                    builder.addRepository(repo);
                }
            } catch (Exception e) {
                log.debug("Failed to add repository as input repository: " + repoUrl + ": " + e.getMessage());
            }
        }

        private String absolute(String path) {
            String prefix = null;
            if(path.startsWith("flat:")){
                prefix = "flat:";
                path = path.substring(5);
            }
            if (!isRemote(path)) {
                File f = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, new File(path)));
                path = f.getPath();
            }
            if(prefix == null)
                return path;
            else
                return prefix + path;
        }

        private boolean isOffline(CeylonConfig config) {
            return offline || DefaultToolOptions.getDefaultOffline(config);
        }

        private int getTimeout(CeylonConfig config) {
            return (timeout >= 0) ? timeout : (int)DefaultToolOptions.getDefaultTimeout(config);
        }
        
        protected Overrides getOverrides(CeylonConfig config) {
            String path = (overrides != null) ? overrides : DefaultToolOptions.getDefaultOverrides(config);
            return getOverrides(path);
        }

        protected Overrides getOverrides(String path) {
            if (path != null) {
                File f = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, new File(path)));
                return getOverrides(f);
            }
            return null;
        }

        protected Overrides getOverrides(File f) {
            return RepositoryManagerBuilder.parseOverrides(f.getPath());
        }
    }

    /**
     * Create and return a new SourceArchiveCreator.
     *
     * @param repoManager   The output RepositoryManager where the .src archive will be placed
     * @param sourcePaths   The root directories that contain source files
     * @param moduleName    The module name, used for the artifact
     * @param moduleVersion The module version, used for the artifact
     * @param verbose       If true, will print additional info about its progress
     * @param log           The CMR logger to use for printing progress info.
     * @throws IOException
     */
    public static ArtifactCreator makeSourceArtifactCreator(RepositoryManager repoManager,
                                                                Iterable<? extends File> sourcePaths, String moduleName, String moduleVersion,
                                                                boolean verbose, Logger log) throws IOException {
        return new SourceArtifactCreatorImpl(repoManager, sourcePaths, moduleName, moduleVersion, verbose, log);
    }

    /**
     * Create and return a new ResourceArtifactCreator.
     *
     * @param repoManager   The output RepositoryManager where the "module-resource" artifact will be placed
     * @param sourcePaths   The root directories that contain source files
     * @param resourcePaths The root directories that contain resource files
     * @param resourceRootName The name of the special resource root folder
     * @param moduleName    The module name, used for the artifact
     * @param moduleVersion The module version, used for the artifact
     * @param verbose       If true, will print additional info about its progress
     * @param log           The CMR logger to use for printing progress info.
     * @throws IOException
     */
    public static ArtifactCreator makeResourceArtifactCreator(RepositoryManager repoManager,
                                                                Iterable<? extends File> sourcePaths,
                                                                Iterable<? extends File> resourcePaths,
                                                                String resourceRootName,
                                                                String moduleName, String moduleVersion,
                                                                boolean verbose, Logger log) throws IOException {
        return new ResourceArtifactCreatorImpl(repoManager, sourcePaths, resourcePaths, resourceRootName, moduleName, moduleVersion, verbose, log);
    }

    public static <T> boolean arrayContains(T[] array, T item) {
        return Arrays.asList(array).contains(item);
    }

    public static boolean isRemote(String token) {
        // IMPORTANT Make sure this is consistent with RepositoryBuilderImpl.buildRepository() !
        // (except for "file:" which we don't support)
        return isHttp(token) || "mvn".equals(token) || token.startsWith("mvn:") || "aether".equals(token) || token.startsWith("aether:") || token.equals("jdk") || token.equals("jdk:");
    }

    public static boolean isHttp(String token) {
        try {
            URL url = new URL(token);
            String protocol = url.getProtocol();
            return "http".equals(protocol) || "https".equals(protocol);
        } catch (MalformedURLException e) {
            return false;
        }
    }
}

