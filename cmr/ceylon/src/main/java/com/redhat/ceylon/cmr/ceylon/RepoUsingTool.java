package com.redhat.ceylon.cmr.ceylon;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.ResourceBundle;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.Messages;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Tool;

public abstract class RepoUsingTool implements Tool {
    protected List<URI> repo;
    protected String systemRepo;
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
    
    public void setRepositoryAsStrings(List<String> repo) throws URISyntaxException {
        if (repo != null) {
            List<URI> result = new ArrayList<URI>(repo.size());
            for (String r : repo) {
                result.add(new URI(r));
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

    @Option(longName="offline")
    @Description("Enables offline mode that will prevent the module loader from connecting to remote repositories.")
    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public void setOut(Appendable out) {
        this.out = out;
    }
    
    protected synchronized RepositoryManager getRepositoryManager() {
        if (rm == null) {
            CeylonUtils.CeylonRepoManagerBuilder rmb = CeylonUtils.repoManager()
                    .systemRepo(systemRepo)
                    .userRepos(getRepositoryAsStrings())
                    .offline(offline);
                
            rm = rmb.buildManager();   
        }
        return rm;
    }

    protected String moduleName(String moduleNameOptVersion) {
        int p = moduleNameOptVersion.indexOf('/');
        if (p == -1) {
            return moduleNameOptVersion;
        } else {
            return moduleNameOptVersion.substring(0, p);
        }
    }
    
    protected String moduleVersion(String moduleNameOptVersion) {
        int p = moduleNameOptVersion.indexOf('/');
        if (p == -1 || (p == (moduleNameOptVersion.length() - 1))) {
            return null;
        } else {
            return moduleNameOptVersion.substring(p + 1);
        }
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
        if ("default".equals(name)) {
            return "";
        }
        Collection<ModuleVersionDetails> versions = getModuleVersions(repoMgr, name, version, type, binaryVersion);
        if (versions.isEmpty() && version != null) {
            // Maybe the user specified the wrong version?
            // Let's see if we can find any and suggest them
            versions = getModuleVersions(repoMgr, name, null, type, binaryVersion);
        }
        if (versions.isEmpty()) {
            errorMsg("module.not.found", name, repoMgr.getRepositoriesDisplayString());
            return null;
        }
        if (versions.size() > 1 || version != null) {
            if (version == null) {
                errorMsg("missing.version", name, repoMgr.getRepositoriesDisplayString());
            } else {
                errorMsg("version.not.found", version, name);
            }
            msg("try.versions");
            boolean first = true;
            for (ModuleVersionDetails mvd : versions) {
                if (!first) {
                    append(", ");
                }
                append(mvd.getVersion());
                first = false;
            }
            newline();
            return null;
        }
        return versions.iterator().next().getVersion();
    }
    
    public RepoUsingTool msg(String msgKey, Object...msgArgs) throws IOException {
        out.append(Messages.msg(bundle, msgKey, msgArgs));
        return this;
    }
    
    public RepoUsingTool errorMsg(String msgKey, Object...msgArgs) throws IOException {
        error.append(Messages.msg(bundle, msgKey, msgArgs));
        error.append(System.lineSeparator());
        return this;
    }
    
    public RepoUsingTool append(Object s) throws IOException {
        out.append(String.valueOf(s));
        return this;
    }
    
    public RepoUsingTool newline() throws IOException {
        out.append(System.lineSeparator());
        return this;
    }
}
