package com.redhat.ceylon.common;

import java.io.File;
import java.util.ArrayList;

public class Repositories {
    private CeylonConfig config;
    
    private static final String SECTION_REPOSITORY = "repository";
    private static final String SECTION_REPOSITORIES = "repositories";

    private static final String REPO_TYPE_BOOTSTRAP = "bootstrap";
    private static final String REPO_TYPE_OUTPUT = "output";
    private static final String REPO_TYPE_CACHE = "cache";
    private static final String REPO_TYPE_LOOKUP = "lookup";
    
    private static final String ITEM_PASSWORD = "password";
    private static final String ITEM_USER = "user";
    private static final String ITEM_URL = "url";
    
    public static final String REPO_URL_CEYLON = "http://modules.ceylon-lang.org/test";

    private static Repositories instance;
    
    public static Repositories get() {
        if (instance == null) {
            instance = new Repositories();
        }
        return instance;
    }
    
    public static void set(Repositories repos) {
        instance = repos;
    }
    
    public static Repositories withConfig(CeylonConfig config) {
        return new Repositories(config);
    }
    
    public static class Repository {
        private final String name;
        private final String url;
        private final String user;
        private final String password;
        
        public String getName() {
            return name;
        }
        
        public String getUrl() {
            return url;
        }
        
        public String getUser() {
            return user;
        }
        
        public String getPassword() {
            return password;
        }
        
        public Repository(String name, String url, String user, String password) {
            this.name = name;
            this.url = url;
            this.user = user;
            this.password = password;
        }
    }
    
    private Repositories() {
        this(CeylonConfig.get());
    }
    
    private Repositories(CeylonConfig config) {
        this.config = config;
    }
    
    private String repoKey(String repoName, String itemName) {
        return SECTION_REPOSITORY + "." + repoName + "." + itemName;
    }
    
    public Repository getRepository(String repoName) {
        String url = config.getOption(repoKey(repoName, ITEM_URL));
        if (url != null) {
            String user = config.getOption(repoKey(repoName, ITEM_USER));
            String password = config.getOption(repoKey(repoName, ITEM_PASSWORD));
            return new Repository(repoName, url, user, password);
        } else {
            return null;
        }
    }
    
    private String reposTypeKey(String repoType) {
        return SECTION_REPOSITORIES + "." + repoType;
    }
    
    private Repository[] getRepositoriesByType(String repoType) {
        String urls[] = config.getOptionValues(reposTypeKey(repoType));
        if (urls != null) {
            ArrayList<Repository> repos = new ArrayList<Repository>(urls.length);
            for (int i = 0; i < urls.length; i++) {
                String url = urls[i];
                Repository repo;
                if (url.startsWith("@")) {
                    String name = url.substring(1);
                    repo = getRepository(name);
                } else {
                    String name = "%" + repoType + "-" + (i + 1);
                    repo = new Repository(name, url, null, null);
                }
                if (repo != null) {
                    repos.add(repo);
                }
            }
            if (!repos.isEmpty()) {
                Repository[] result = new Repository[repos.size()];
                return repos.toArray(result);
            }
        }
        return null;
    }
    
    private Repository getRepositoryByType(String repoType) {
        Repository[] repos = getRepositoriesByType(repoType);
        if (repos != null) {
            return repos[0];
        } else {
            return null;
        }
    }
    
    public File getUserRepoDir() {
        String ceylonUserRepo = System.getProperty("ceylon.user.repo");
        if (ceylonUserRepo != null) {
            return new File(ceylonUserRepo);
        } else {
            File userDir = config.getUserDir();
            return new File(userDir, "repo");
        }
    }
    
    public Repository getBootstrapRepository() {
        Repository repo = getRepositoryByType(REPO_TYPE_BOOTSTRAP);
        if (repo == null) {
            File installDir = config.getInstallDir();
            if (installDir != null) {
                // $INSTALLDIR/repo
                File dir = new File(installDir, "repo");
                repo = new Repository(REPO_TYPE_BOOTSTRAP, dir.getAbsolutePath(), null, null);
            }
        }
        return repo;
    }
    
    public Repository getOutputRepository() {
        Repository repo = getRepositoryByType(REPO_TYPE_OUTPUT);
        if (repo == null) {
            // ./modules
            File dir = new File(".", "modules");
            repo = new Repository(REPO_TYPE_OUTPUT, dir.getPath(), null, null);
        }
        return repo;
    }
    
    public Repository getCacheRepository() {
        Repository repo = getRepositoryByType(REPO_TYPE_CACHE);
        if (repo == null) {
            // $HOME/.ceylon/repo
            File dir = getUserRepoDir();
            repo = new Repository(REPO_TYPE_CACHE, dir.getAbsolutePath(), null, null);
        }
        return repo;
    }
    
    public Repository[] getLookupRepositories() {
        Repository[] repos = getRepositoriesByType(REPO_TYPE_LOOKUP);
        if (repos == null) {
            repos = new Repository[3];
            // ./modules
            File modulesDir = new File(".", "modules");
            repos[0] = new Repository(REPO_TYPE_LOOKUP + "1", modulesDir.getPath(), null, null);
            // $HOME/.ceylon/repo
            File userRepoDir = getUserRepoDir();
            repos[1] = new Repository(REPO_TYPE_LOOKUP + "2", userRepoDir.getAbsolutePath(), null, null);
            // http://modules.ceylon-lang.org
            repos[2] = new Repository(REPO_TYPE_LOOKUP + "3", REPO_URL_CEYLON, null, null);
        }
        return repos;
    }
}
