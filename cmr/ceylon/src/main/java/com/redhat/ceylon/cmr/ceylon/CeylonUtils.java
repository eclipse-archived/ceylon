package com.redhat.ceylon.cmr.ceylon;

import com.redhat.ceylon.cmr.api.*;
import com.redhat.ceylon.cmr.impl.*;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.config.Repositories;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class CeylonUtils {

    public static CeylonRepoManagerBuilder repoManager() {
        return new CeylonRepoManagerBuilder();
    }

    public static class CeylonRepoManagerBuilder {
        private File actualCwd;
        private File cwd;
        private String systemRepo;
        private String cacheRepo;
        private List<String> userRepos;
        private List<String> extraUserRepos;
        private List<String> remoteRepos;
        private String outRepo;
        private String user;
        private String password;
        private boolean offline;
        private boolean noDefRepos;
        private boolean jdkIncluded;
        private Logger log;

        /**
         * Sets the current working directory to use for encountering the configuration
         * files to use for building the repository manager. When not set the current
         * directory as defined by <code>new File(".")</code> will be used
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
         * Sets the current working directory to use for encountering the configuration
         * files to use for building the repository manager. When not set the current
         * directory as defined by <code>new File(".")</code> will be used
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
                log = new JULLogger();
            }

            // Make sure we load the correct configuration

            CeylonConfig config;
            actualCwd = new File(".");
            if (cwd == null || actualCwd.equals(cwd)) {
                cwd = actualCwd;
                config = CeylonConfig.get();
            } else {
                config = CeylonConfig.createFromLocalDir(cwd);
            }
            Repositories repositories = Repositories.withConfig(config);

            // First we determine the cache repository

            File root;
            if (cacheRepo == null) {
                Repositories.Repository cr = repositories.getCacheRepository();
                root = new File(absolute(cr.getUrl()));
            } else {
                root = new File(absolute(resolveRepoUrl(repositories, cacheRepo)));
            }

            final RepositoryManagerBuilder builder = new RepositoryManagerBuilder(root, log, isOffline(config));

            // The first two we add in reverse order because they get PREpended to the root

            if (outRepo == null) {
                addRepo(builder, repositories.getOutputRepository(), true);
            } else {
                addRepo(builder, repositories, outRepo, true);
            }

            if (systemRepo == null) {
                addRepo(builder, repositories.getSystemRepository(), true);
            } else {
                addRepo(builder, repositories, systemRepo, true);
            }

            // The rest we add in the normal order becuase they get APpended to the root

            if (jdkIncluded) {
                addRepo(builder, repositories, "jdk", false);
            }

            if (userRepos != null && !userRepos.isEmpty()) {
                // Add user defined repos
                for (String repo : userRepos) {
                    addRepo(builder, repositories, repo, false);
                }
            } else {
                // We add the configured local lookup repos only when no user defined repos have been passed
                if (!noDefRepos) {
                    Repositories.Repository[] repos = repositories.getLocalLookupRepositories();
                    for (Repositories.Repository lookup : repos) {
                        addRepo(builder, lookup, false);
                    }
                }
            }

            // Add the extra user defined repos
            if (extraUserRepos != null && !extraUserRepos.isEmpty()) {
                for (String repo : extraUserRepos) {
                    addRepo(builder, repositories, repo, false);
                }
            }
                
            // Add default non-remote repos (like the user repo)
            if (!noDefRepos) {
                Repositories.Repository[] globals = repositories.getGlobalLookupRepositories();
                for (Repositories.Repository lookup : globals) {
                    addRepo(builder, lookup, false);
                }
            }

            // Add the "remote" repos (not necessarily remote but it's at the point in the
            // lookup order where you'd normally define remote repos)
            if (remoteRepos != null && !remoteRepos.isEmpty()) {
                // Add remote repos
                for (String repo : remoteRepos) {
                    addRepo(builder, repositories, repo, false);
                }
            } else {
                // We add the configured remote lookup repos only when no user defined repos have been passed
                if (!noDefRepos) {
                    Repositories.Repository[] repos = repositories.getRemoteLookupRepositories();
                    for (Repositories.Repository lookup : repos) {
                        addRepo(builder, lookup, false);
                    }
                }
            }

            // Add the remaining ("other") default repos (like the Herd repo),
            // these will always come last
            if (!noDefRepos) {
                Repositories.Repository[] others = repositories.getOtherLookupRepositories();
                for (Repositories.Repository lookup : others) {
                    addRepo(builder, lookup, false);
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
                log = new JULLogger();
            }

            // Make sure we load the correct configuration

            actualCwd = new File(".");
            if (cwd == null) {
                cwd = actualCwd;
            }
            CeylonConfig config = CeylonConfig.createFromLocalDir(cwd);
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

            if (!isHTTP(outRepo)) {
                File repoFolder = new File(absolute(outRepo));
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
                File cachingDir = FileUtil.makeTempDir("ceylonc");

                // HTTP
                WebDAVContentStore davContentStore = new WebDAVContentStore(outRepo, log, false);
                davContentStore.setUsername(user);
                davContentStore.setPassword(password);

                return new CachingRepositoryManager(davContentStore, cachingDir, log);
            }
        }

        private void addRepo(RepositoryManagerBuilder builder, Repositories.Repository repoInfo, boolean prepend) {
            if (repoInfo != null) {
                try {
                    String path = absolute(repoInfo.getUrl());
                    Repository repo = builder.repositoryBuilder().buildRepository(path);
                    if (prepend) {
                        builder.prependRepository(repo);
                    } else {
                        builder.appendRepository(repo);
                    }
                } catch (Exception e) {
                    log.debug("Failed to add repository as input repository: " + repoInfo.getUrl() + ": " + e.getMessage());
                }
            }
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
        
        private void addRepo(RepositoryManagerBuilder builder, Repositories repositories, String repoUrl, boolean prepend) {
            try {
                if (repoUrl.startsWith("+")) {
                    // The token is the name of a repository defined in the Ceylon configuration file
                    String path = absolute(repoUrl.substring(1));
                    Repositories.Repository repo = repositories.getRepository(path);
                    if (repo != null) {
                        repoUrl = repo.getUrl();
                    }
                }
                String path = absolute(repoUrl);
                Repository repo = builder.repositoryBuilder().buildRepository(path);
                if (prepend) {
                    builder.prependRepository(repo);
                } else {
                    builder.appendRepository(repo);
                }
            } catch (Exception e) {
                log.debug("Failed to add repository as input repository: " + repoUrl + ": " + e.getMessage());
            }
        }

        private String absolute(String path) {
            if (!isRemote(path)) {
                File f = new File(path);
                if (!f.isAbsolute()) {
                    f = new File(cwd, path);
                    try {
                        path = f.getCanonicalPath();
                    } catch (IOException e) {
                        path = f.getAbsolutePath();
                    }
                }
            }
            return path;
        }

        private boolean isHTTP(String repo) {
            try {
                URL url = new URL(repo);
                String protocol = url.getProtocol();
                return "http".equals(protocol) || "https".equals(protocol);
            } catch (MalformedURLException e) {
                return false;
            }
        }

        private boolean isRemote(String repo) {
            // IMPORTANT Make sure this is consistent with RepositoryBuilderImpl.buildRepository() !
            // (except for "file:" which we don't support)
            return isHTTP(repo) || "mvn".equals(repo) || repo.startsWith("mvn:") || "aether".equals(repo) || repo.startsWith("aether:") || repo.equals("jdk") || repo.equals("jdk:");
        }
        
        private boolean isOffline(CeylonConfig config) {
            return offline || config.getBoolOption(DefaultToolOptions.DEFAULTS_OFFLINE, false);
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
    public static SourceArchiveCreator makeSourceArchiveCreator(RepositoryManager repoManager,
                                                                Iterable<? extends File> sourcePaths, String moduleName, String moduleVersion,
                                                                boolean verbose, Logger log) throws IOException {
        return new SourceArchiveCreatorImpl(repoManager, sourcePaths, moduleName, moduleVersion, verbose, log);
    }

    public static <T> boolean arrayContains(T[] array, T item) {
        return Arrays.asList(array).contains(item);
    }

}

