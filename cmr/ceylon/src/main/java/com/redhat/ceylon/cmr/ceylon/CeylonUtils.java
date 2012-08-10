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
import com.redhat.ceylon.cmr.impl.SimpleRepositoryManager;
import com.redhat.ceylon.cmr.impl.SourceArchiveCreatorImpl;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.Repositories;

public class CeylonUtils {

    public static RepositoryManagerBuilder makeRepositoryManagerBuilder(List<String> userRepos, String outRepo, Logger log) {
        final RepositoryManagerBuilder builder = new RepositoryManagerBuilder(log);

        log.debug("Repository lookup order:");
        
        appendRepo(builder, Repositories.get().getBootstrapRepository(), log);

        if (outRepo == null) {
            appendRepo(builder, Repositories.get().getOutputRepository(), log);
        } else {
            appendRepo(builder, outRepo, log);
        }
        
        if (userRepos.isEmpty()) {
            Repositories.Repository[] lookups = Repositories.get().getLookupRepositories();
            for (Repositories.Repository lookup : lookups) {
                appendRepo(builder, lookup, log);
            }
        } else {
            for (int i=0; i<userRepos.size(); i++) {
                String repo = userRepos.get(i);
                appendRepo(builder, repo, log);
            }
        }
        
        return builder;
    }

    public static RepositoryManager makeRepositoryManager(List<String> userRepos, String outRepo, Logger log) {
        return makeRepositoryManagerBuilder(userRepos, outRepo, log).buildRepository();
    }

    public static RepositoryManager makeOutputRepositoryManager(String outRepo, Logger log, String user, String password) {
        if (outRepo == null) {
            Repositories.Repository repo = Repositories.get().getOutputRepository();
            outRepo = repo.getUrl();
            if (user == null) {
                user = repo.getUser();
            }
            if (password == null) {
                password = repo.getPassword();
            }
        } else {
            if (outRepo.startsWith("+")) {
                // The token is the name of a repository defined in the Ceylon configuration file
                Repositories.Repository repo = Repositories.get().getRepository(outRepo.substring(1));
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

    private static boolean isHTTP(String repo, Logger log) {
        try {
            URL url = new URL(repo);
            String protocol = url.getProtocol();
            return "http".equals(protocol) || "https".equals(protocol);
        } catch (MalformedURLException e) {
            log.debug("Invalid repo URL: "+repo+" (assuming file)");
            return false;
        }
    }

    private static void appendRepo(RepositoryManagerBuilder builder, Repositories.Repository repoInfo, Logger log) {
        if (repoInfo != null) {
            try {
                log.debug(" - " + repoInfo.getName() + " = " + repoInfo.getUrl());
                Repository repo = builder.repositoryBuilder().buildRepository(repoInfo.getUrl());
                builder.appendRepository(repo);
            } catch(Exception e) {
                log.debug("Failed to add repository as input repository: " + repoInfo.getUrl() + ": "+e.getMessage());
            }
        }
    }

    private static void appendRepo(RepositoryManagerBuilder builder, String repoUrl, Logger log) {
        try {
            if (repoUrl.startsWith("+")) {
                // The token is the name of a repository defined in the Ceylon configuration file
                Repositories.Repository repo = Repositories.get().getRepository(repoUrl.substring(1));
                if (repo != null) {
                    appendRepo(builder, repo, log);
                    return;
                }
            }
            Repository repo = builder.repositoryBuilder().buildRepository(repoUrl);
            log.debug(" - " + repoUrl);
            builder.appendRepository(repo);
        } catch(Exception e) {
            log.debug("Failed to add repository as input repository: " + repoUrl + ": "+e.getMessage());
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
