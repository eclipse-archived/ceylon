package com.redhat.ceylon.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;

import com.redhat.ceylon.common.ant.Rep;

/**
 * Baseclass for tasks which only do something if source files are newer than
 * corresponding module artifacts in the output repository.
 * @author tom
 */
abstract class LazyTask extends Task {

    private Path src;
    private String out;
    private Boolean noMtimeCheck = false;
    private Rep systemRepository;
    private List<Rep> repositories = new LinkedList<Rep>();
    private final Task task = this;
    
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

    protected List<File> getSrc() {
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
     * Adds a module repository
     * @param rep the new module repository
     */
    public void addRep(Rep rep) {
        systemRepository = rep;
    }

    protected Rep getSystemRepository() {
        return systemRepository;
    }

    /**
     * Sets the system repository
     * @param rep the new system repository
     */
    public void setSysRep(Rep rep) {
        repositories.add(rep);
    }

    protected List<Rep> getRepositories() {
        return repositories;
    }

    /**
     * Set the destination repository into which the Java source files should be
     * compiled.
     * @param out the destination repository
     */
    public void setOut(String out) {
        this.out = out;
    }

    protected String getOut() {
        if (this.out == null) {
            return new File(task.getProject().getBaseDir(), "modules").getPath();
        }
        return this.out;
    }
    
}
