package com.redhat.ceylon.ant;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.tools.ant.types.DataType;

/**
 * Runtime representation of a {@code <reposet>} element, which can be 
 * defined at the top level of an ant project and referred to by other tasks.
 */
public class RepoSet extends DataType {

    private final LinkedHashSet<Repo> repos = new LinkedHashSet<Repo>();
    
    public void addConfiguredRepo(Repo repo) {
        repos.add(repo);
    }
    
    public void addConfiguredRepoSet(RepoSet reposet) {
        repos.addAll(reposet.getRepos());
    }
    
    public Set<Repo> getRepos() {
        LinkedHashSet<Repo> result = new LinkedHashSet<Repo>();
        result.addAll(this.repos);

        if (getRefid() != null) {
            RepoSet referredRepoSet = (RepoSet)getProject().getReference(getRefid().getRefId());
            result.addAll(referredRepoSet.getRepos());        
        }
        return result;
    }
    
}
