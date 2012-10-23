package com.redhat.ceylon.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.redhat.ceylon.common.ant.Rep;
import com.redhat.ceylon.common.config.Repositories;
import com.redhat.ceylon.compiler.js.CeylonRunJsException;
import com.redhat.ceylon.compiler.js.CeylonRunJsTool;

/** Ant task to run Ceylon code compiled to JS from ant.
 * 
 * @author Enrique Zamudio
 */
public class Ceylonjs extends Task {

    private String nodePath;
    private String module;
    private String func = "run";
    private List<Rep> repos = new LinkedList<Rep>();
    private Rep sysrepo;

    /**
     * Adds a module repository
     * @param rep the new module repository
     */
    public void addRep(Rep rep) {
        repos.add(rep);
    }
    protected List<Rep> getRepositories() {
        return repos;
    }

    protected Rep getSystemRepository() {
        return sysrepo;
    }

    /**
     * Sets the system repository
     * @param rep the new system repository
     */
    public void setSysRep(Rep rep) {
        sysrepo = rep;
    }

    public void setModule(String value) {
        module = value;
    }
    public void setRun(String value) {
        func = value;
    }
    public void setNodePath(String value) {
        nodePath = value;
    }

    @Override
    public void execute() throws BuildException {
        if (nodePath == null) {
            try {
                nodePath = CeylonRunJsTool.findNode();
            } catch (CeylonRunJsException ex) {
                throw new BuildException("Cannot find Node.js executable", ex);
            }
        }
        if (module == null) {
            throw new BuildException("ceylonjs requires module attribute to be set");
        }
        if (func == null) {
            throw new BuildException("ceylonjs requires method attribute to be set");
        }
        List<String> reps = new ArrayList<String>(repos.size());
        for (Rep r : repos) {
            reps.add(r.toString());
        }
        if (reps.isEmpty()) {
            //Use defaults
            reps.add("modules");
            File r = Repositories.get().getSystemRepoDir();
            if (r != null) {
                reps.add(r.getAbsolutePath());
            }
            r = Repositories.get().getUserRepoDir();
            if (r != null) {
                reps.add(r.getAbsolutePath());
            }
        }
        try {
            CeylonRunJsTool runner = new CeylonRunJsTool();
            runner.setRepositories(reps);
            runner.setModuleVersion(module);
            runner.setRun(func);
            runner.run();
        } catch (Exception ex) {
            throw new BuildException(ex);
        }
    }
}
