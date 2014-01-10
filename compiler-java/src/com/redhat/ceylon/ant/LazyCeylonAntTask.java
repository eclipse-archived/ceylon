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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import com.redhat.ceylon.common.Constants;


/**
 * Baseclass for tasks which only do something if source files are newer than
 * corresponding module artifacts in the output repository.
 * @see LazyHelper
 * @author tom
 */
abstract class LazyCeylonAntTask extends CeylonAntTask implements Lazy {

    private Path src;
    private Path res;
    private String out;
    private String encoding;
    private String user;
    private String pass;
    private Boolean noMtimeCheck = false;
    private String systemRepository;
    private RepoSet reposet = new RepoSet();
    private final Task task = this;
    
    protected LazyCeylonAntTask(String toolName) {
        super(toolName);
    }
    
    /**
     * Sets the user name for the output module repository (HTTP only)
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Sets the password for the output module repository (HTTP only)
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
    
    /**
     * Sets whether a file modification time check should be performed
     * @param noMtimeCheck
     */
    public void setNoMtimeCheck(Boolean noMtimeCheck) {
        this.noMtimeCheck = noMtimeCheck;
    }
    
    public boolean getNoMtimeCheck() {
        return noMtimeCheck;
    }

    /**
     * Set the source directories to find the source Java and Ceylon files.
     * @param src the source directories as a path
     */
    public void setSrc(Path src) {
        if (this.src == null) {
            this.src = src;
        } else {
            this.src.append(src);
        }
    }

    public List<File> getSrc() {
        if (this.src == null) {
            return Collections.singletonList(task.getProject().resolveFile(Constants.DEFAULT_SOURCE_DIR));
        }
        String[] paths = this.src.list();
        ArrayList<File> result = new ArrayList<File>(paths.length);
        for (String path : paths) {
            result.add(task.getProject().resolveFile(path));
        }
        return result;
    }

    /**
     * Set the resource directories to find the resource files.
     * @param res the resource directories as a path
     */
    public void setResource(Path res) {
        if (this.res == null) {
            this.res = res;
        } else {
            this.res.append(res);
        }
    }

    public List<File> getResource() {
        if (this.res == null) {
            return Collections.singletonList(task.getProject().resolveFile(Constants.DEFAULT_RESOURCE_DIR));
        }
        String[] paths = this.res.list();
        ArrayList<File> result = new ArrayList<File>(paths.length);
        for (String path : paths) {
            result.add(task.getProject().resolveFile(path));
        }
        return result;
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
     * @param rep the new system repository
     */
    public void setSysRep(String rep) {
        systemRepository = rep;
    }

    /**
     * Set the destination repository into which the Java source files should be
     * compiled.
     * @param out the destination repository
     */
    public void setOut(String out) {
        this.out = out;
    }

    public String getOut() {
        if (this.out == null) {
            return new File(task.getProject().getBaseDir(), Constants.DEFAULT_MODULE_DIR).getPath();
        }
        return this.out;
    }

    /**
     * Set the encoding for the the source files.
     * @param out Charset encoding name
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    protected void completeCommandline(Commandline cmd) {
        if (out != null) {
            appendOptionArgument(cmd, "--out", out);
        }

        if (encoding != null) {
            appendOptionArgument(cmd, "--encoding", encoding);
        } else  {
            log(getLocation().getFileName() + ":" +getLocation().getLineNumber() + ": No encoding specified, this might cause problems with portability to other platforms!", Project.MSG_WARN);
        }
        
        appendUserOption(cmd, user);
        appendPassOption(cmd, pass);
        
        for (File src : getSrc()) {
            cmd.createArgument().setValue("--source=" + src.getAbsolutePath());
        }
        
        if (getSystemRepository() != null) {
            cmd.createArgument().setValue("--sysrep=" + getSystemRepository());
        }
        
        for(Repo rep : getReposet()){
            // skip empty entries
            if(rep.url == null || rep.url.isEmpty())
                continue;
            log("Adding repository: "+rep, Project.MSG_VERBOSE);
            appendOptionArgument(cmd, "--rep", rep.url);
        }
    }
    
}
