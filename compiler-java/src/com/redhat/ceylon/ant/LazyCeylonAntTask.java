package com.redhat.ceylon.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;


/**
 * Baseclass for tasks which only do something if source files are newer than
 * corresponding module artifacts in the output repository.
 * @see LazyHelper
 * @author tom
 */
abstract class LazyCeylonAntTask extends CeylonAntTask implements Lazy {

    private Path src;
    private String out;
    private Boolean noMtimeCheck = false;
    private String systemRepository;
    private RepoSet reposet = new RepoSet();
    private final Task task = this;
    
    protected LazyCeylonAntTask(String toolName) {
        super(toolName);
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
            return Collections.singletonList(task.getProject().resolveFile("source"));
        }
        String[] paths = this.src.list();
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
            return new File(task.getProject().getBaseDir(), "modules").getPath();
        }
        return this.out;
    }
    
}
