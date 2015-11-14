/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ant;

import java.util.Set;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

/**
 * Baseclass for tasks that use repositories
 * @author tom, tako
 */
abstract class RepoUsingCeylonAntTask extends CeylonAntTask {

    private RepoSet reposet = new RepoSet();
    private String systemRepository;
    private String cacheRepository;
    private String overrides;
    private Boolean noDefaultRepositories = false;
    private Boolean offline = false;
    
    protected RepoUsingCeylonAntTask(String toolName) {
        super(toolName);
    }
    
    /**
     * Adds a module repository for a {@code <rep>} nested element
     * @param rep the new module repository
     */
    public void addConfiguredRep(Repo rep) {
        reposet.addConfiguredRepo(rep);
    }
    /**
     * Adds a set of module repositories for a {@code <reposet>} nested element
     * @param reposet the new module repository
     */
    public void addConfiguredReposet(RepoSet reposet) {
        this.reposet.addConfiguredRepoSet(reposet);
    }
    
    protected Set<Repo> getReposet() {
        return reposet.getRepos();
    }

    protected String getSystemRepository() {
        return systemRepository;
    }

    /**
     * Sets the system repository
     * @param rep path to the new system repository
     */
    public void setSysRep(String rep) {
        systemRepository = rep;
    }

    protected String getCacheRepository() {
        return cacheRepository;
    }

    /**
     * Sets the cache repository
     * @param rep path to the new cache repository
     */
    public void setCacheRep(String rep) {
        cacheRepository = rep;
    }

    protected String getOverrides() {
        return overrides;
    }

    /**
     * Sets the path to the module overrides XML file.
     * @param overrides the path to the module overrides XML file
     * @deprecated use {@link #setOverrides(String)}
     */
    public void setMavenOverrides(String overrides) {
        this.overrides = overrides;
    }

    /**
     * Sets the path to the module overrides XML file.
     * @param overrides the path to the module overrides XML file
     */
    public void setOverrides(String overrides) {
        this.overrides = overrides;
    }

    /**
     * Wether to use the default list of repositories or not
     * @param noDefaultRepositories
     */
    public void setNoDefaultRepositories(Boolean noDefaultRepositories) {
        this.noDefaultRepositories = noDefaultRepositories;
    }
    
    public boolean getNoDefaultRepositories() {
        return noDefaultRepositories;
    }
    
    /**
     * Sets offline mode for remote repository handling
     * @param offline
     */
    public void setOffline(Boolean offline) {
        this.offline = offline;
    }
    
    public boolean getOffline() {
        return offline;
    }

    @Override
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if (getSystemRepository() != null) {
            appendOptionArgument(cmd, "--sysrep", getSystemRepository());
        }
        
        if (getCacheRepository() != null) {
            appendOptionArgument(cmd, "--cacherep", getCacheRepository());
        }
        
        if (getOverrides() != null) {
            appendOptionArgument(cmd, "--overrides", getOverrides());
        }
        
        if (offline) {
            appendOption(cmd, "--offline");
        }
        
        if (noDefaultRepositories) {
            appendOption(cmd, "--no-default-repositories");
        }
        
        for (Repo rep : getReposet()) {
            // skip empty entries
            if(rep.url == null || rep.url.isEmpty())
                continue;
            log("Adding repository: "+rep, Project.MSG_VERBOSE);
            appendOptionArgument(cmd, "--rep", rep.url);
        }
    }
    
}
