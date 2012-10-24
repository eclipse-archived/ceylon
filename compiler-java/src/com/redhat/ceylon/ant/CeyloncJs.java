package com.redhat.ceylon.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import com.redhat.ceylon.common.ant.Module;
import com.redhat.ceylon.common.ant.Rep;
import com.redhat.ceylon.common.tool.Java7Checker;

public class CeyloncJs extends Task {

    private List<File> compileList = new ArrayList<File>(2);
    private List<Module> modules = new LinkedList<Module>();
    private FileSet files;
    private boolean verbose;
    private boolean optimize;
    private boolean modulify = true;
    private boolean gensrc = true;
    private String user;
    private String pass;
    private String out;
    private Path src;
    private Rep systemRepository;
    private List<Rep> repositories = new LinkedList<Rep>();
    private ExitHandler exitHandler = new ExitHandler();
    private File executable;

    /**
     * Set compiler executable depending on the OS.
     */
    public void setExecutable(String executable) {
        this.executable = new File(Util.getScriptName(executable));
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

    /** Tells the JS compiler whether to wrap the generated code in CommonJS module format. */
    public void setWrapModule(boolean flag){
        modulify = flag;
    }
    /** Tells the JS compiler whether to use prototype style or not. */
    public void setOptimize(boolean flag){
        this.optimize = flag;
    }
    /** Tells the JS compiler whether to generate the .src archive; default is true, but can be turned off
     * to save some time when doing joint jvm/js compilation. */
    public void setSrcArchive(boolean flag) {
        gensrc = flag;
    }
    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }

    /**
     * Adds a module to compile
     * @param module the module name to compile
     */
    public void addModule(Module module){
        modules.add(module);
    }

    public void addFiles(FileSet fileset) {
        if (this.files != null) {
            throw new BuildException("<ceyloncjs> only supports a single <files> element");
        }
        this.files = fileset;
    }

    /**
     * Clear the list of files to be compiled and copied..
     */
    protected void resetFileLists() {
        compileList.clear();
    }

    /**
     * Check that all required attributes have been set and nothing silly has
     * been entered.
     *
     * @exception BuildException if an error occurs
     */
    protected void checkParameters() throws BuildException {
        if (this.modules.isEmpty()
                && this.files == null) {
            throw new BuildException("You must specify a <module> and/or <files>");
        }
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
            return Collections.singletonList(getProject().resolveFile("source"));
        }
        String[] paths = this.src.list();
        ArrayList<File> result = new ArrayList<File>(paths.length);
        for (String path : paths) {
            result.add(getProject().resolveFile(path));
        }
        return result;
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
            return new File(getProject().getBaseDir(), "modules").getPath();
        }
        return this.out;
    }

    /**
     * Adds a module repository
     * @param rep the new module repository
     */
    public void addRep(Rep rep) {
        repositories.add(rep);
    }

    protected Rep getSystemRepository() {
        return systemRepository;
    }

    /**
     * Sets the system repository
     * @param rep the new system repository
     */
    public void setSysRep(Rep rep) {
        systemRepository = rep;
    }

    protected List<Rep> getRepositories() {
        return repositories;
    }

    /**
     * Executes the task.
     * @exception BuildException if an error occurs
     */
    @Override
    public void execute() throws BuildException {
        Java7Checker.check();

        checkParameters();
        resetFileLists();

        if (files != null) {

            for (File srcDir : getSrc()) {
                FileSet fs = (FileSet)this.files.clone();
                fs.setDir(srcDir);
                if (!srcDir.exists()) {
                    throw new BuildException("source path \"" + srcDir.getPath() + "\" does not exist!", getLocation());
                }

                DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                String[] files = ds.getIncludedFiles();

                for(String fileName : files)
                    compileList.add(new File(srcDir, fileName));
            }
        }

        compile();
    }

    /**
     * Perform the compilation.
     */
    private void compile() {
        if (compileList.size() == 0 && modules.size() == 0){
            log("Nothing to compile");
            return;
        }

        Commandline cmd = new Commandline();
        cmd.setExecutable(Util.findCeylonScript(this.executable, getProject()));
        cmd.createArgument().setValue("compile-js");
        if(verbose){
            cmd.createArgument().setValue("--verbose");
        }
        if(user != null){
            cmd.createArgument().setValue("--user=" + user);
        }
        if(pass != null){
            cmd.createArgument().setValue("--pass=" + pass);
        }

        cmd.createArgument().setValue("--out=" + getOut());

        for (File src : getSrc()) {
            cmd.createArgument().setValue("--src=" + src.getAbsolutePath());
        }
        if (getSystemRepository() != null) {
            cmd.createArgument().setValue("--sysrep=" + Util.quoteParameter(getSystemRepository().url));
        }
        for(Rep rep : getRepositories()){
            // skip empty entries
            if(rep.url == null || rep.url.isEmpty())
                continue;
            log("Adding repository: "+rep, Project.MSG_VERBOSE);
            cmd.createArgument().setValue("--rep=" + Util.quoteParameter(rep.url));
        }
        for (File file : compileList) {
            log("Adding source file: "+file.getAbsolutePath(), Project.MSG_VERBOSE);
            cmd.createArgument().setValue(file.getAbsolutePath());
        }
        // modules to compile
        for (Module module : modules) {
            log("Adding module: "+module, Project.MSG_VERBOSE);
            cmd.createArgument().setValue(module.toSpec());
        }
        if (optimize) {
            cmd.createArgument().setValue("--optimize");
        }
        if (!modulify) {
            cmd.createArgument().setValue("--no-module");
        }
        if (!gensrc) {
            cmd.createArgument().setValue("--skip-src-archive");
        }

        try {
            Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN));
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            log("Command line " + Arrays.toString(cmd.getCommandline()), Project.MSG_VERBOSE);
            exe.setCommandline(cmd.getCommandline());
            exe.execute();
            if (exe.getExitValue() != 0) {
                exitHandler.handleExit(this, exe.getExitValue(), Ceylonc.FAIL_MSG);
            }
        } catch (IOException e) {
            throw new BuildException("Error running Ceylon compiler", e, getLocation());
        }
    }

}
