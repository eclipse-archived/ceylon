package com.redhat.ceylon.cmr.ceylon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.api.SourceArchiveCreator;
import com.redhat.ceylon.cmr.impl.CachingRepositoryManager;
import com.redhat.ceylon.cmr.impl.FileContentStore;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.cmr.impl.SimpleRepositoryManager;
import com.redhat.ceylon.cmr.impl.SourceArchiveCreatorImpl;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.Repositories;

public class CeylonUtils {

    public static CeylonRepoManagerBuilder repoManager() {
        return new CeylonRepoManagerBuilder();
    }
    
    public static class CeylonRepoManagerBuilder {
        private File cwd;
        private String systemRepo;
        private List<String> userRepos;
        private String outRepo;
        private String user;
        private String password;
        private Logger log;
        
        public CeylonRepoManagerBuilder cwd(File cwd) {
            this.cwd = cwd;
            return this;
        }
        
        public CeylonRepoManagerBuilder systemRepo(String systemRepo) {
            this.systemRepo = systemRepo;
            return this;
        }
        
        public CeylonRepoManagerBuilder userRepos(List<String> userRepos) {
            this.userRepos = userRepos;
            return this;
        }
        
        public CeylonRepoManagerBuilder outRepo(String outRepo) {
            this.outRepo = outRepo;
            return this;
        }
        
        public CeylonRepoManagerBuilder user(String user) {
            this.user = user;
            return this;
        }
        
        public CeylonRepoManagerBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public CeylonRepoManagerBuilder logger(Logger logger) {
            this.log = logger;
            return this;
        }

        public RepositoryManager buildManager() {
            return buildManagerBuilder().buildRepository();
        }

        public RepositoryManagerBuilder buildManagerBuilder() {
            if (log == null) {
                log = new JULLogger();
            }
            final RepositoryManagerBuilder builder = new RepositoryManagerBuilder(log);

            // Make sure we load the correct configuration
            
            if (cwd == null) {
                cwd = new File(".");
            }
            CeylonConfig config = CeylonConfig.createFromLocalDir(cwd);
            Repositories repositories = Repositories.withConfig(config);
            
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
            
            if (userRepos != null && !userRepos.isEmpty()) {
                // Add user defined repos
                for (int i=0; i<userRepos.size(); i++) {
                    String repo = userRepos.get(i);
                    addRepo(builder, repositories, repo, false);
                }
            } else {
                // We add local lookup repos only when no user defined repois have been passed
                Repositories.Repository[] lookups = repositories.getLocalLookupRepositories();
                for (Repositories.Repository lookup : lookups) {
                    addRepo(builder, lookup, false);
                }
            }
            
            // Add globally defined repos (like the user repo and the default remote Herd repo)
            Repositories.Repository[] lookups = repositories.getGlobalLookupRepositories();
            for (Repositories.Repository lookup : lookups) {
                addRepo(builder, lookup, false);
            }
            
            log.debug("Repository lookup order:");
            for (String rds : builder.getRepositoriesDisplayString()) {
                log.debug(" - " + rds);
            }
            
            return builder;
        }

        public RepositoryManager buildOutputManager() {
            if (log == null) {
                log = new JULLogger();
            }
            
            // Make sure we load the correct configuration
            
            if (cwd == null) {
                cwd = new File(".");
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
            
            if (!isHTTP(outRepo, log)) {
                File repoFolder = new File(outRepo);
                if (repoFolder.exists()) {
                    if (!repoFolder.isDirectory()) {
                        log.error("Output repository is not a directory: "+outRepo);
                    } else if (!repoFolder.canWrite()) {
                        log.error("Output repository is not writable: "+outRepo);
                    }
                } else if(!repoFolder.mkdirs()) {
                    log.error("Failed to create output repository: "+outRepo);
                }
                StructureBuilder structureBuilder = new FileContentStore(repoFolder);
                return new SimpleRepositoryManager(structureBuilder, log);
            }else{
                File cachingDir = FileUtil.makeTempDir("ceylonc");

                // HTTP
                WebDAVContentStore davContentStore = new WebDAVContentStore(outRepo, log);
                davContentStore.setUsername(user);
                davContentStore.setPassword(password);

                return new CachingRepositoryManager(davContentStore, cachingDir, log);
            }
        }

        private boolean isHTTP(String repo, Logger log) {
            try {
                URL url = new URL(repo);
                String protocol = url.getProtocol();
                return "http".equals(protocol) || "https".equals(protocol);
            } catch (MalformedURLException e) {
                log.debug("Invalid repo URL: "+repo+" (assuming file)");
                return false;
            }
        }

        private void addRepo(RepositoryManagerBuilder builder, Repositories.Repository repoInfo, boolean prepend) {
            if (repoInfo != null) {
                try {
                    Repository repo = builder.repositoryBuilder().buildRepository(repoInfo.getUrl());
                    if (prepend) {
                        builder.prependRepository(repo);                
                    } else {
                        builder.appendRepository(repo);
                    }
                } catch(Exception e) {
                    log.debug("Failed to add repository as input repository: " + repoInfo.getUrl() + ": "+e.getMessage());
                }
            }
        }

        private void addRepo(RepositoryManagerBuilder builder, Repositories repositories, String repoUrl, boolean prepend) {
            try {
                if (repoUrl.startsWith("+")) {
                    // The token is the name of a repository defined in the Ceylon configuration file
                    Repositories.Repository repo = repositories.getRepository(repoUrl.substring(1));
                    if (repo != null) {
                        addRepo(builder, repo, prepend);
                        return;
                    }
                }
                Repository repo = builder.repositoryBuilder().buildRepository(repoUrl);
                if (prepend) {
                    builder.prependRepository(repo);                
                } else {
                    builder.appendRepository(repo);
                }
            } catch(Exception e) {
                log.debug("Failed to add repository as input repository: " + repoUrl + ": "+e.getMessage());
            }
        }
    }

    /** Create and return a new SourceArchiveCreator.
     * @param repoManager The output RepositoryManager where the .src archive will be placed
     * @param sourcePaths The root directories that contain source files
     * @param moduleName The module name, used for the artifact
     * @param moduleVersion The module version, used for the artifact
     * @param verbose If true, will print additional info about its progress
     * @param log The CMR logger to use for printing progress info.
     * @throws IOException */
    public static SourceArchiveCreator makeSourceArchiveCreator(RepositoryManager repoManager,
            Iterable<? extends File> sourcePaths, String moduleName, String moduleVersion,
            boolean verbose, Logger log) throws IOException {
        return new SourceArchiveCreatorImpl(repoManager, sourcePaths, moduleName, moduleVersion, verbose, log);
    }

}

