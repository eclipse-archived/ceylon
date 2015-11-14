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
