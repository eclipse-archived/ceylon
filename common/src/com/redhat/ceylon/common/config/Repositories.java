package com.redhat.ceylon.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;

/**
 * Repositories is a wrapper around a CeylonConfig object providing easy access to
 * repository related configuration information. 
 * 
 * Ceylon uses a set of local and remote repositories for its modules.
 * The order and significance of the lookup (which is fixed) is:
 * 
 *   system - Essential system modules
 *   cache  - A cache of modules that were previously downloaded from remote repositories
 *   output - Where the compiler stores newly created modules
 *   lookup - User-defined local repositories
 *   global - Predefined non-remote repositories
 *   remote - User defined remote repositories
 *   other  - Predefined remote repositories (like Herd)
 *   
 * The "system" section by default contains a reference to the system global repository,
 * which normally is something like "%CEYLONHOME%/repo". Do not override.
 * 
 * The "cache" section is where modules downloaded from remote repositories are kept so they
 * don't have to be downloaded each time. By default this points to "%USER_HOME%/.ceylon/cache".
 * 
 *  The "output" section is where newly compiled modules are stored. By default this is "./modules".
 *  
 *  The "lookup" section is for modules local to the project and other user-defined
 *  repositories. By default this is "./modules".
 *  
 *  The "global" section is meant for pre-defined, essential, non-remote repositories.
 *  By default this is "%USER_HOME%/.ceylon/repo". It's normally not advisable to override this.
 *  
 *  The "remote" section is meant for user-defined remote repositories. By default this is empty.
 * 
 *  The "other" section is meant for pre-defined, essential, remote repositories. By default
 *  this is points to the official Ceylon Herd repository. It's normally not advisable to
 *  override this.
 *  
 * The [repositories] section in a configuration file can be used to override the default
 * values for those entries thereby changing or extending the lookup order. Take a look at
 * the following example:
 * 
 *   [repositories]
 *   output=./output # Store new modules in the local `output` folder
 *   cache=/huge-disk/tom/ceylon/repocache # Store the cached modules on a bigger disk
 *   lookup=./modules
 *   lookup=./extra-modules
 *   lookup=/usr/local/ceylon/even-more-modules
 *   remote=http://ceylon.example.com # An external site with Ceylon modules
 *
 * First of all, the values for ouput and cache (as well as system, but you should normally
 * never try overriding it) can only be specified once, while the others (lookup, global and
 * remote) can be specified multiple times, Ceylon will try them one by one in the order you
 * specify in this list.
 * 
 * NB: When we say "in the order you specify" we refer to the ones with the same key name,
 * so if you add several remote repositories they will be tried in the order you specify,
 * but you cannot change the main ordering: lookup repositories will always be tried before
 * global, which will always be tried before remote.
 * 
 * The remote entry doesn't have any default value, so it can be easily used without having to
 * worry about pre-existing values. It's specifically meant to add extra (normally remote)
 * respositories that will be tried after all other options have been exhausted.
 * 
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class Repositories {
    private CeylonConfig config;
    
    private static final String SECTION_REPOSITORY = "repository";
    private static final String SECTION_REPOSITORIES = "repositories";

    public static final String REPO_TYPE_SYSTEM = "system";
    public static final String REPO_TYPE_OUTPUT = "output";
    public static final String REPO_TYPE_CACHE = "cache";
    public static final String REPO_TYPE_LOCAL_LOOKUP = "lookup";
    public static final String REPO_TYPE_GLOBAL_LOOKUP = "global";
    public static final String REPO_TYPE_REMOTE_LOOKUP = "remote";
    public static final String REPO_TYPE_OTHER_LOOKUP = "other";
    
    private static final String REPO_NAME_SYSTEM = "SYSTEM";
    private static final String REPO_NAME_LOCAL = "LOCAL";
    private static final String REPO_NAME_CACHE = "CACHE";
    private static final String REPO_NAME_USER = "USER";
    private static final String REPO_NAME_REMOTE = "REMOTE";
    
    private static final String ITEM_PASSWORD = "password";
    private static final String ITEM_PASSWORD_KS = "password-keystore";
    private static final String ITEM_PASSWORD_KS_ALIAS = "password-alias";
    private static final String ITEM_USER = "user";
    private static final String ITEM_URL = "url";
    
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
    
    public interface Repository {
        public String getName();
        public String getUrl();
        public Credentials getCredentials();
    }
    
    public static class SimpleRepository implements Repository {

        private final String name;
        private final String url;
        private final Credentials credentials;
        
        public String getName() {
            return name;
        }
        
        public String getUrl() {
            return url;
        }
        
        public Credentials getCredentials() {
            return credentials;
        }

        public SimpleRepository(String name, String url, Credentials credentials) {
            this.name = name;
            this.url = url;
            this.credentials = credentials;
        }
    }
    
    public static class RepositoryRef implements Repository {
        private final Repository ref;
        
        public String getName() {
            return ref.getName();
        }
        
        public String getUrl() {
            return ref.getUrl();
        }
        
        public Credentials getCredentials() {
            return ref.getCredentials();
        }

        public RepositoryRef(Repository ref) {
            this.ref= ref;
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
            final String alias = config.getOption(repoKey(repoName, ITEM_PASSWORD_KS_ALIAS));
            String keystore = config.getOption(repoKey(repoName, ITEM_PASSWORD_KS));
            String prompt = ConfigMessages.msg("repository.password.prompt", user, url);
            Credentials credentials = Credentials.create(user, password, keystore, alias, prompt);
            return new SimpleRepository(repoName, url, credentials);
        } else {
            if (REPO_NAME_SYSTEM.equals(repoName)) {
                File dir = getSystemRepoDir();
                if (dir != null) {
                    // $INSTALLDIR/repo
                    return new SimpleRepository(REPO_NAME_SYSTEM, dir.getAbsolutePath(), null);
                }
            } else if (REPO_NAME_LOCAL.equals(repoName)) {
                // ./modules
                File dir = new File(".", Constants.DEFAULT_MODULE_DIR);
                return new SimpleRepository(REPO_NAME_LOCAL, dir.getPath(), null);
            } else if (REPO_NAME_CACHE.equals(repoName)) {
                // $HOME/.ceylon/cache
                File dir = getCacheRepoDir();
                return new SimpleRepository(REPO_NAME_CACHE, dir.getAbsolutePath(), null);
            } else if (REPO_NAME_USER.equals(repoName)) {
                // $HOME/.ceylon/repo
                File userRepoDir = getUserRepoDir();
                return new SimpleRepository(REPO_NAME_USER, userRepoDir.getAbsolutePath(), null);
            } else if (REPO_NAME_REMOTE.equals(repoName)) {
                // http://modules.ceylon-lang.org
                return new SimpleRepository(REPO_NAME_REMOTE, Constants.REPO_URL_CEYLON, null);
            }
            return null;
        }
    }
    
    private String reposTypeKey(String repoType) {
        return SECTION_REPOSITORIES + "." + repoType;
    }
    
    public Repository[] getRepositoriesByType(String repoType) {
        String urls[] = config.getOptionValues(reposTypeKey(repoType));
        if (urls != null) {
            ArrayList<Repository> repos = new ArrayList<Repository>(urls.length);
            for (int i = 0; i < urls.length; i++) {
                String url = urls[i];
                Repository repo;
                if (url.startsWith("+")) {
                    String name = url.substring(1);
                    repo = new RepositoryRef(getRepository(name));
                } else {
                    String name = "%" + repoType + "-" + (i + 1);
                    repo = new SimpleRepository(name, url, null);
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
    
    public void setRepositoriesByType(String repoType, Repository[] repos) {
        String key = reposTypeKey(repoType);
        if (repos != null) {
            ArrayList<String> urls = new ArrayList<String>(repos.length);
            for (Repository repo : repos) {
                String url;
                if (repo instanceof RepositoryRef) {
                    url = "+" + repo.getName();
                } else {
                    url = repo.getUrl();
                }
                urls.add(url);
            }
            if (!urls.isEmpty()) {
                String[] values = new String[urls.size()];
                config.setOptionValues(key, urls.toArray(values));
            } else {
                config.removeOption(key);
            }
        } else {
            config.removeOption(key);
        }
    }
    
    public Map<String, Repository[]> getRepositories() {
        HashMap<String, Repository[]> repos = new HashMap<String, Repository[]>();
        repos.put(REPO_TYPE_SYSTEM, getRepositoriesByType(REPO_TYPE_SYSTEM));
        repos.put(REPO_TYPE_OUTPUT, getRepositoriesByType(REPO_TYPE_OUTPUT));
        repos.put(REPO_TYPE_CACHE, getRepositoriesByType(REPO_TYPE_CACHE));
        repos.put(REPO_TYPE_LOCAL_LOOKUP, getRepositoriesByType(REPO_TYPE_LOCAL_LOOKUP));
        repos.put(REPO_TYPE_GLOBAL_LOOKUP, getRepositoriesByType(REPO_TYPE_GLOBAL_LOOKUP));
        repos.put(REPO_TYPE_REMOTE_LOOKUP, getRepositoriesByType(REPO_TYPE_REMOTE_LOOKUP));
        repos.put(REPO_TYPE_OTHER_LOOKUP, getRepositoriesByType(REPO_TYPE_OTHER_LOOKUP));
        return repos;
    }
    
    public void setRepositories(Map<String, Repository[]> repos) {
        setRepositoriesByType(REPO_TYPE_SYSTEM, repos.get(REPO_TYPE_SYSTEM));
        setRepositoriesByType(REPO_TYPE_OUTPUT, repos.get(REPO_TYPE_OUTPUT));
        setRepositoriesByType(REPO_TYPE_CACHE, repos.get(REPO_TYPE_CACHE));
        setRepositoriesByType(REPO_TYPE_LOCAL_LOOKUP, repos.get(REPO_TYPE_LOCAL_LOOKUP));
        setRepositoriesByType(REPO_TYPE_GLOBAL_LOOKUP, repos.get(REPO_TYPE_GLOBAL_LOOKUP));
        setRepositoriesByType(REPO_TYPE_REMOTE_LOOKUP, repos.get(REPO_TYPE_REMOTE_LOOKUP));
        setRepositoriesByType(REPO_TYPE_OTHER_LOOKUP, repos.get(REPO_TYPE_OTHER_LOOKUP));
    }
    
    public File getSystemRepoDir() {
        String ceylonSystemRepo = System.getProperty(Constants.PROP_CEYLON_SYSTEM_REPO);
        if (ceylonSystemRepo != null) {
            return new File(ceylonSystemRepo);
        } else {
            File userDir = FileUtil.getInstallDir();
            if (userDir != null) {
                return new File(userDir, "repo");
            }
        }
        return null;
    }
    
    public File getUserRepoDir() {
        String ceylonUserRepo = System.getProperty(Constants.PROP_CEYLON_USER_REPO);
        if (ceylonUserRepo != null) {
            return new File(ceylonUserRepo);
        } else {
            File userDir = FileUtil.getUserDir();
            return new File(userDir, "repo");
        }
    }
    
    public File getCacheRepoDir() {
        String ceylonUserRepo = System.getProperty(Constants.PROP_CEYLON_CACHE_REPO);
        if (ceylonUserRepo != null) {
            return new File(ceylonUserRepo);
        } else {
            File userDir = FileUtil.getUserDir();
            return new File(userDir, "cache");
        }
    }
    
    public Repository getSystemRepository() {
        Repository repo = getRepositoryByType(REPO_TYPE_SYSTEM);
        if (repo == null) {
            repo = getRepository(REPO_NAME_SYSTEM);
        }
        return repo;
    }
    
    public Repository getOutputRepository() {
        Repository repo = getRepositoryByType(REPO_TYPE_OUTPUT);
        if (repo == null) {
            repo = getRepository(REPO_NAME_LOCAL);
        }
        return repo;
    }
    
    public Repository getCacheRepository() {
        Repository repo = getRepositoryByType(REPO_TYPE_CACHE);
        if (repo == null) {
            repo = getRepository(REPO_NAME_CACHE);
        }
        return repo;
    }
    
    public Repository[] getLocalLookupRepositories() {
        Repository[] repos = getRepositoriesByType(REPO_TYPE_LOCAL_LOOKUP);
        if (repos == null) {
            repos = new Repository[1];
            // By default "./modules"
            repos[0] = getRepository(REPO_NAME_LOCAL);
        }
        return repos;
    }
    
    public Repository[] getGlobalLookupRepositories() {
        Repository[] repos = getRepositoriesByType(REPO_TYPE_GLOBAL_LOOKUP);
        if (repos == null) {
            repos = new Repository[1];
            // By default "$HOME/.ceylon/repo"
            repos[0] = getRepository(REPO_NAME_USER);
        }
        return repos;
    }
    
    public Repository[] getRemoteLookupRepositories() {
        Repository[] repos = getRepositoriesByType(REPO_TYPE_REMOTE_LOOKUP);
        if (repos == null) {
            repos = new Repository[0];
        }
        return repos;
    }
    
    public Repository[] getOtherLookupRepositories() {
        Repository[] repos = getRepositoriesByType(REPO_TYPE_OTHER_LOOKUP);
        if (repos == null) {
            repos = new Repository[1];
            // By default "http://modules.ceylon-lang.org"
            repos[0] = getRepository(REPO_NAME_REMOTE);
        }
        return repos;
    }
}
