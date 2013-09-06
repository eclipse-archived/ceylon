package com.redhat.ceylon.cmr.ceylon;

import java.io.IOException;
import java.net.URI;
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
import com.redhat.ceylon.common.Versions;
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
            return null;
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

    protected Collection<ModuleVersionDetails> getModuleVersions(String nameAndOptionalVersion, boolean compatible, boolean offline) {
        String name;
        String version;
        int p = nameAndOptionalVersion.indexOf('/');
        if (p == -1) {
            name = nameAndOptionalVersion;
            version = null;
        } else {
            name = nameAndOptionalVersion.substring(0, p);
            version = nameAndOptionalVersion.substring(p + 1);
        }
        return getModuleVersions(name, version, compatible, offline);
    }

    protected Collection<ModuleVersionDetails> getModuleVersions(String name, String version, boolean compatible, boolean offline) {
        ModuleVersionQuery query = new ModuleVersionQuery(name, version, ModuleQuery.Type.JVM);
        if (compatible) {
            query.setBinaryMajor(Versions.JVM_BINARY_MAJOR_VERSION);
        }
        ModuleVersionResult result = getRepositoryManager().completeVersions(query);
        NavigableMap<String, ModuleVersionDetails> versionMap = result.getVersions();
        return versionMap.values();
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
