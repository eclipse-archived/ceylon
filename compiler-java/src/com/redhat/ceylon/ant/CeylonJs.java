package com.redhat.ceylon.ant;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;

import com.redhat.ceylon.common.ant.Rep;

public class CeylonJs extends Task {

    private String module;
    private String func;
    private List<Rep> repos = new LinkedList<Rep>();
    private Rep sysrepo;
    private File executable;
    private Rep systemRepository;
    private ExitHandler exitHandler = new ExitHandler();

    /**
     * Set compiler executable depending on the OS.
     */
    public void setExecutable(String executable) {
        this.executable = new File(Util.getScriptName(executable));
    }

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

    @Override
    public void execute() throws BuildException {
        if (module == null) {
            throw new BuildException("ceylonjs requires module attribute to be set");
        }
        try {
            Commandline cmd = new Commandline();
            cmd.setExecutable(Util.findCeylonScript(this.executable, getProject()));
            cmd.createArgument().setValue("run");
            if(func != null){
                cmd.createArgument().setValue("--run=" + func);
            }
            if (systemRepository != null) {
                cmd.createArgument().setValue("--sysrep=" + Util.quoteParameter(systemRepository.url));
            }
            if(repos != null){
                for(Rep rep : repos) {
                    // skip empty entries
                    if (rep.url == null || rep.url.isEmpty())
                        continue;
                    cmd.createArgument().setValue("--rep="+Util.quoteParameter(rep.url));
                }
            }
            cmd.createArgument().setValue(module);
            Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN));
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            log("Command line " + Arrays.toString(cmd.getCommandline()), Project.MSG_VERBOSE);
            exe.setCommandline(cmd.getCommandline());
            exe.execute();
            if (exe.getExitValue() != 0) {
                exitHandler.handleExit(this, exe.getExitValue(), Ceylon.FAIL_MSG);
            }
        } catch (IOException ex) {
            throw new BuildException(ex);
        }
    }
}
